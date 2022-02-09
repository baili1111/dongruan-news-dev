package com.dongruan.user.controller;

import com.dongruan.api.controller.BaseController;
import com.dongruan.api.controller.user.AppUserMngControllerApi;
import com.dongruan.api.controller.user.HelloControllerApi;
import com.dongruan.constant.SystemConstant;
import com.dongruan.enums.UserStatus;
import com.dongruan.grace.result.GraceJSONResult;
import com.dongruan.grace.result.ResponseStatusEnum;
import com.dongruan.pojo.AppUser;
import com.dongruan.user.service.AppUserMngService;
import com.dongruan.user.service.UserService;
import com.dongruan.utils.PagedGridResult;
import com.dongruan.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author zhu
 * @date 2022/1/16 17:12:50
 * @description
 */
@RestController
public class AppUserMngController extends BaseController implements AppUserMngControllerApi {

	final static Logger logger = LoggerFactory.getLogger(AppUserMngController.class);

	@Autowired
	private AppUserMngService appUserMngService;

	@Autowired
	private UserService userService;

	@Override
	public GraceJSONResult queryAll(String nickname, Integer status, Date startDate,
	                                Date endDate, Integer page, Integer pageSize) {
		//System.out.println(startDate);
		//System.out.println(endDate);


		if (page == null) {
			page = SystemConstant.COMMON_START_PAGE;
		}

		if (pageSize == null) {
			pageSize = SystemConstant.COMMON_PAGE_SIZE;
		}

		PagedGridResult result = appUserMngService.queryAllUserList(nickname, status, startDate,
				endDate, page, pageSize);

		return GraceJSONResult.ok(result);
	}

	@Override
	public GraceJSONResult userDetail(String userId) {
		AppUser user = userService.getUser(userId);
		return GraceJSONResult.ok(user);
	}

	@Override
	public GraceJSONResult freezeUserOrNot(String userId, Integer doStatus) {

		if (!UserStatus.isUserStatusValid(doStatus)) {
			return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_STATUS_ERROR);
		}
		appUserMngService.freezeUserOrNot(userId, doStatus);

		// 刷新用户状态：
		// 方式一：删除用户会话，以保障用户需要重新登录来刷新他的状态，
		// 方式二：查询最新用户信息，重新放入redis，这种方式不太好，因为会话信息应该要让用户自己去创建的，
		//        admin最好不要干预，这也是为什么很多网站的客服大多都会让你重新登录系统再去其他的操作，目的就是重置会话信息
		redis.del(SystemConstant.REDIS_USER_INFO + ":" + userId);

		return GraceJSONResult.ok();
	}
}
