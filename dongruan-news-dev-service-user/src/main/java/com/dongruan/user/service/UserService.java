package com.dongruan.user.service;

import com.dongruan.pojo.AppUser;
import com.dongruan.pojo.bo.UpdateUserInfoBO;

/**
 * @author zhu
 * @date 2022/1/19 23:41:41
 * @description
 */
public interface UserService {

	/**
	 * 判断用户是否存在
	 */
	public AppUser queryMobileIsExist(String mobile);

	/**
	 * 创建用户，新增用户记录到数据库
	 */
	public AppUser createUser(String mobile);

	/**
	 * 根据用户主键id获得用户信息
	 */
	public AppUser getUser(String userId);

	/**
	 * 用户修改信息，完善资料，并且激活
	 */
	public void updateUserInfo(UpdateUserInfoBO updateUserInfoBO);


}
