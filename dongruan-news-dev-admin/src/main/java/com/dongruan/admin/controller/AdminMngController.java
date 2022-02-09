package com.dongruan.admin.controller;

import com.dongruan.admin.service.AdminUserService;
import com.dongruan.api.controller.BaseController;
import com.dongruan.api.controller.admin.AdminMngControllerApi;
import com.dongruan.constant.SystemConstant;
import com.dongruan.enums.FaceVerifyType;
import com.dongruan.exception.GraceException;
import com.dongruan.grace.result.GraceJSONResult;
import com.dongruan.grace.result.ResponseStatusEnum;
import com.dongruan.pojo.AdminUser;
import com.dongruan.pojo.bo.AdminLoginBO;
import com.dongruan.pojo.bo.NewAdminBO;
import com.dongruan.utils.FaceVerifyUtils;
import com.dongruan.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author zhu
 * @date 2022/1/16 17:12:50
 * @description
 */
@RestController
public class AdminMngController extends BaseController implements AdminMngControllerApi {

	final static Logger logger = LoggerFactory.getLogger(AdminMngController.class);

	@Autowired
	private AdminUserService adminUserService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private FaceVerifyUtils faceVerifyUtils;

	@Override
	public GraceJSONResult adminLogin(AdminLoginBO adminLoginBO,
	                                  HttpServletRequest request,
	                                  HttpServletResponse response) {

		// 0.验证BO中的用户名和密码不为空


		// 1.查询admin用户的信息
		AdminUser admin = adminUserService.queryAdminByUsername(adminLoginBO.getUsername());

		// 2.判断admin不为空，如果为空则登录失败
		if (admin == null) {
			return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
		}

		// 3.判断密码是否匹配
		boolean isPwdMatch = BCrypt.checkpw(adminLoginBO.getPassword(), admin.getPassword());
		if (isPwdMatch) {
			doLoginSettings(admin, request, response);
			return GraceJSONResult.ok();
		} else {
			return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
		}

	}

	@Override
	public GraceJSONResult adminIsExist(String username) {

		checkAdminExist(username);

		return GraceJSONResult.ok();
	}


	@Override
	public GraceJSONResult addNewAdmin(NewAdminBO newAdminBO,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response) {

		// 0.验证BO中的用户名和密码不为空

		// 1. base64不为空，代表人脸登录，否则密码不能为空
		if (StringUtils.isBlank(newAdminBO.getImg64())) {
			if (StringUtils.isBlank(newAdminBO.getPassword()) || StringUtils.isBlank(newAdminBO.getConfirmPassword())) {
				return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
			}
		}

		// 2. 密码不为空，必须判断两次输入一致
		if (!StringUtils.isBlank(newAdminBO.getPassword())) {
			if (!newAdminBO.getPassword().equalsIgnoreCase(newAdminBO.getConfirmPassword())) {
				return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
			}
		}

		// 3.校验用户名唯一
		checkAdminExist(newAdminBO.getUsername());

		// 4.调用service存入admin信息
		adminUserService.createAdminUser(newAdminBO);

		return GraceJSONResult.ok();
	}

	@Override
	public GraceJSONResult getAdminList(Integer page, Integer pageSize) {
		if (page == null) {
			page = SystemConstant.COMMON_START_PAGE;
		}

		if (pageSize == null) {
			pageSize = SystemConstant.COMMON_PAGE_SIZE;
		}

		PagedGridResult result = adminUserService.queryAdminList(page, pageSize);

		return GraceJSONResult.ok(result);
	}

	@Override
	public GraceJSONResult adminLogout(String adminId, HttpServletRequest request, HttpServletResponse response) {

		// 从redis中删除admin的会话token
		redis.del(SystemConstant.REDIS_ADMIN_TOKEN + ":" + adminId);

		// 从cookie中清理admin登录的相关信息
		deleteCookie(request, response, "atoken");
		deleteCookie(request, response, "aid");
		deleteCookie(request, response, "aname");

		return GraceJSONResult.ok();
	}


	@Override
	public GraceJSONResult adminFaceLogin(AdminLoginBO adminLoginBO,
	                                      HttpServletRequest request,
	                                      HttpServletResponse response) {
		// 0. 判断用户名和人脸信息不能为空
		if (StringUtils.isBlank(adminLoginBO.getUsername())) {
			return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_USERNAME_NULL_ERROR);
		}
		String tempFace64 = adminLoginBO.getImg64();
		if (StringUtils.isBlank(tempFace64)) {
			return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_FACE_NULL_ERROR);
		}

		// 1. 从数据库中查询admin信息，获得人脸faceId
		AdminUser admin = adminUserService.queryAdminByUsername(adminLoginBO.getUsername());
		String adminFaceId = admin.getFaceId();

		if (StringUtils.isBlank(adminFaceId)) {
			return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_FACE_LOGIN_ERROR);
		}

		// 2. 请求文件服务，获取人脸的base64信息
		String fileServerUrlExecute = "http://files.imoocnews.com:8004/fs/readFace64InGridFS?faceId=" + adminFaceId;
		ResponseEntity<GraceJSONResult> resultEntity = restTemplate.getForEntity(fileServerUrlExecute, GraceJSONResult.class);
		GraceJSONResult graceJSONResult = resultEntity.getBody();
		String base64DB = (String)graceJSONResult.getData();
//        System.out.println("restTemplate远程调用获得的内容为：" + base64);

		// 3. 调用阿里人脸识别只能AI接口，对比人脸实现登录
		boolean result = faceVerifyUtils.faceVerify(FaceVerifyType.BASE64.type,
				tempFace64,
				base64DB,
				60);

		if (!result) {
			return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_FACE_LOGIN_ERROR);
		}


		// 4. 设置管理员信息到redis与cookie
		doLoginSettings(admin, request, response);

		return GraceJSONResult.ok();
	}


	private void checkAdminExist(String username) {
		AdminUser admin = adminUserService.queryAdminByUsername(username);
		if (admin != null) {
			GraceException.display(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
		}

	}

	/**
	 * 用于 admin 用户登录过后的基本信息设置
	 * @param admin
	 * @param request
	 * @param response
	 */
	private void doLoginSettings(AdminUser admin, HttpServletRequest request, HttpServletResponse response) {

		// 保存token放入到redis中
		String token = UUID.randomUUID().toString();
		redis.set(SystemConstant.REDIS_ADMIN_TOKEN + ":" + admin.getId(), token);

		// 保存admin登录基本token信息到cookie中
		setCookie(request, response, "atoken", token, SystemConstant.COOKIE_MONTH);
		setCookie(request, response, "aid", admin.getId(), SystemConstant.COOKIE_MONTH);
		setCookie(request, response, "aname", admin.getAdminName(), SystemConstant.COOKIE_MONTH);

	}
}
