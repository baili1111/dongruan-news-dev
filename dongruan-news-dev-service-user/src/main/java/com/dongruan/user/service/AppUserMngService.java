package com.dongruan.user.service;

import com.dongruan.pojo.AppUser;
import com.dongruan.pojo.bo.UpdateUserInfoBO;
import com.dongruan.utils.PagedGridResult;

import java.util.Date;

/**
 * @author zhu
 * @date 2022/1/19 23:41:41
 * @description
 */
public interface AppUserMngService {

	/**
	 * 查询用户列表
	 */
	public PagedGridResult queryAllUserList(String nickname, Integer status, Date startDate,
	                                        Date endDate, Integer page, Integer pageSize);


	/**
	 * 冻结用户账号，或解除封号状态
	 */
	public void freezeUserOrNot(String userId, Integer status);


}
