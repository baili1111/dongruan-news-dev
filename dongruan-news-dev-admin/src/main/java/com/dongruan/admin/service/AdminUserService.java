package com.dongruan.admin.service;

import com.dongruan.pojo.AdminUser;
import com.dongruan.pojo.bo.NewAdminBO;
import com.dongruan.utils.PagedGridResult;

/**
 * @author zhu
 * @date 2022/1/24 22:30:39
 * @description
 */
public interface AdminUserService {

	/**
	 * 获得管理员的用户信息
	 */
	public AdminUser queryAdminByUsername(String username);

	/**
	 * 新增管理员
	 */
	public void createAdminUser(NewAdminBO newAdminBO);

	/**
	 * 分页查询admin列表
	 * @return
	 */
	public PagedGridResult queryAdminList(Integer page, Integer pageSize);
}
