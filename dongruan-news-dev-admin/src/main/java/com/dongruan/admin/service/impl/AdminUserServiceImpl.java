package com.dongruan.admin.service.impl;

import com.dongruan.admin.mapper.AdminUserMapper;
import com.dongruan.admin.service.AdminUserService;
import com.dongruan.api.service.BaseService;
import com.dongruan.exception.GraceException;
import com.dongruan.grace.result.ResponseStatusEnum;
import com.dongruan.pojo.AdminUser;
import com.dongruan.pojo.bo.NewAdminBO;
import com.dongruan.utils.PagedGridResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author zhu
 * @date 2022/1/24 22:31:54
 * @description
 */
@Service
public class AdminUserServiceImpl extends BaseService implements AdminUserService {

	@Autowired
	public AdminUserMapper adminUserMapper;

	@Autowired
	private Sid sid;

	@Override
	public AdminUser queryAdminByUsername(String username) {

		Example adminExample = new Example(AdminUser.class);
		Example.Criteria criteria = adminExample.createCriteria();
		criteria.andEqualTo("username", username);

		AdminUser admin = adminUserMapper.selectOneByExample(adminExample);

		return admin;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void createAdminUser(NewAdminBO newAdminBO) {
		String adminId = sid.nextShort();

		AdminUser adminUser = new AdminUser();
		adminUser.setId(adminId);
		adminUser.setUsername(newAdminBO.getUsername());
		adminUser.setAdminName(newAdminBO.getAdminName());

		// 如果密码不为空，则需要加密密码，存入数据库
		if (StringUtils.isNotBlank(newAdminBO.getPassword())) {
			String pwd = BCrypt.hashpw(newAdminBO.getPassword(), BCrypt.gensalt());
			adminUser.setPassword(pwd);
		}

		// 如果人脸上传以后，则有faceId，需要和admin信息关联存储入库
		if (StringUtils.isNotBlank(newAdminBO.getFaceId())) {
			adminUser.setFaceId(newAdminBO.getFaceId());
		}

		adminUser.setCreatedTime(new Date());
		adminUser.setUpdatedTime(new Date());

		int result = adminUserMapper.insert(adminUser);

		if (result != 1) {
			GraceException.display(ResponseStatusEnum.ADMIN_CREATE_ERROR);
		}
	}

	@Override
	public PagedGridResult queryAdminList(Integer page, Integer pageSize) {
		Example adminExample = new Example(AdminUser.class);
		adminExample.orderBy("createdTime").desc();

		PageHelper.startPage(page, pageSize);
		List<AdminUser> adminUserList = adminUserMapper.selectByExample(adminExample);

		return setterPageGrid(adminUserList, page);
	}


}
