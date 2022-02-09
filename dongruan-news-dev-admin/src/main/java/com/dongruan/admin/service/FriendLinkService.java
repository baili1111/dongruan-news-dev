package com.dongruan.admin.service;

import com.dongruan.pojo.mo.FriendLinkMO;

import java.util.List;

/**
 * @author zhu
 * @date 2022/1/28 22:54:13
 * @description
 */
public interface FriendLinkService {

	/**
	 * 新增或者更新友情链接
	 */
	void saveOrUpdateFriendLink(FriendLinkMO friendLinkMO);

	/**
	 * 查询友情链接
	 */
	List<FriendLinkMO> queryAllFriendLinkList();


	/**
	 * 删除友情链接
	 */
	void delete(String linkId);

	/**
	 * 首页查询友情链接
	 */
	List<FriendLinkMO> queryPortalFriendLinkList();
}
