package com.dongruan.user.service.impl;

import com.dongruan.constant.SystemConstant;
import com.dongruan.enums.Sex;
import com.dongruan.enums.UserStatus;
import com.dongruan.exception.GraceException;
import com.dongruan.grace.result.ResponseStatusEnum;
import com.dongruan.pojo.AppUser;
import com.dongruan.pojo.bo.UpdateUserInfoBO;
import com.dongruan.user.mapper.AppUserMapper;
import com.dongruan.user.service.UserService;
import com.dongruan.utils.DateUtil;
import com.dongruan.utils.DesensitizationUtil;
import com.dongruan.utils.JsonUtils;
import com.dongruan.utils.RedisOperator;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;


/**
 * @author zhu
 * @date 2022/1/19 23:49:53
 * @description
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private AppUserMapper appUserMapper;

	@Autowired
	private Sid sid;

	@Autowired
	public RedisOperator redis;



	@Override
	public AppUser queryMobileIsExist(String mobile) {

		Example userExample = new Example(AppUser.class);
		Example.Criteria userCriteria = userExample.createCriteria();
		userCriteria.andEqualTo("mobile", mobile);
		AppUser user = appUserMapper.selectOneByExample(userExample);

		return user;
	}

	@Transactional
	@Override
	public AppUser createUser(String mobile) {

		/**
		 * 互联网项目都要考虑可扩展性，
		 * 如果未来业务发展，需要分库分表，
		 * 那么数据库表主键id必须保证全局（全库）唯一，不得重复
		 */
		String userId = sid.nextShort();

		AppUser user = new AppUser();

		user.setId(userId);
		user.setMobile(mobile);
		user.setNickname("用户：" + DesensitizationUtil.commonDisplay(mobile));
		user.setFace(SystemConstant.USER_FACE0);

		user.setBirthday(DateUtil.stringToDate("1900-01-01"));
		user.setSex(Sex.secret.type);
		user.setActiveStatus(UserStatus.INACTIVE.type);

		user.setTotalIncome(0);
		user.setCreatedTime(new Date());
		user.setUpdatedTime(new Date());

		appUserMapper.insert(user);

		return user;
	}

	@Override
	public AppUser getUser(String userId) {
		return appUserMapper.selectByPrimaryKey(userId);
	}

	@Override
	public void updateUserInfo(UpdateUserInfoBO updateUserInfoBO) {
		String userId = updateUserInfoBO.getId();

		// 保证双写一致，先删除redis中的数据，后更新数据库
		redis.del(SystemConstant.REDIS_USER_INFO + ":" + userId);

		AppUser userInfo = new AppUser();
		BeanUtils.copyProperties(updateUserInfoBO, userInfo);

		userInfo.setUpdatedTime(new Date());
		userInfo.setActiveStatus(UserStatus.ACTIVE.type);

		int result = appUserMapper.updateByPrimaryKeySelective(userInfo);
		if (result != 1) {
			GraceException.display(ResponseStatusEnum.USER_UPDATE_ERROR);
		}

		// 再次查询用户的最新信息，放入redis中
		AppUser user = getUser(userId);
		redis.set(SystemConstant.REDIS_USER_INFO + ":" + userId, JsonUtils.objectToJson(user));

		// 缓存双删策略
		try {
			Thread.sleep(100);
			redis.del(SystemConstant.REDIS_USER_INFO + ":" + userId);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
