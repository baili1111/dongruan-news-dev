package com.dongruan.user.service.impl;

import com.dongruan.api.service.BaseService;
import com.dongruan.constant.SystemConstant;
import com.dongruan.enums.Sex;
import com.dongruan.enums.UserStatus;
import com.dongruan.exception.GraceException;
import com.dongruan.grace.result.ResponseStatusEnum;
import com.dongruan.pojo.AppUser;
import com.dongruan.pojo.bo.UpdateUserInfoBO;
import com.dongruan.user.mapper.AppUserMapper;
import com.dongruan.user.service.AppUserMngService;
import com.dongruan.user.service.UserService;
import com.dongruan.utils.*;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
 * @author zhu
 * @date 2022/1/19 23:49:53
 * @description
 */
@Service
public class AppUserMngServiceImpl extends BaseService implements AppUserMngService {

	@Autowired
	private AppUserMapper appUserMapper;


	@Override
	public PagedGridResult queryAllUserList(String nickname, Integer status, Date startDate,
	                                        Date endDate, Integer page, Integer pageSize) {

		Example example = new Example(AppUser.class);
		example.orderBy("createdTime").desc();
		Example.Criteria criteria = example.createCriteria();

		if (StringUtils.isNotBlank(nickname)) {
			criteria.andLike("nickname", "%" + nickname + "%");
		}

		if (UserStatus.isUserStatusValid(status)) {
			criteria.andEqualTo("activeStatus", status);
		}

		if (startDate != null) {
			criteria.andGreaterThanOrEqualTo("createdTime", startDate);
		}

		if (endDate != null) {
			criteria.andLessThanOrEqualTo("createdTime", endDate);
		}

		PageHelper.startPage(page, pageSize);

		List<AppUser> list = appUserMapper.selectByExample(example);

		return setterPageGrid(list, page);
	}

	@Transactional
	@Override
	public void freezeUserOrNot(String userId, Integer status) {
		AppUser user = new AppUser();
		user.setId(userId);
		user.setActiveStatus(status);
		appUserMapper.updateByPrimaryKeySelective(user);
	}
}
