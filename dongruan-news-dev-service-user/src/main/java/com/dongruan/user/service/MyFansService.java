package com.dongruan.user.service;

import com.dongruan.enums.Sex;
import com.dongruan.pojo.AppUser;
import com.dongruan.pojo.bo.UpdateUserInfoBO;
import com.dongruan.pojo.vo.RegionRatioVO;
import com.dongruan.utils.PagedGridResult;

import java.util.List;

/**
 * @author zhu
 * @date 2022/1/19 23:41:41
 * @description
 */
public interface MyFansService {

	/**
	 * 查询当前用户是否关注作家
	 */
	boolean isMeFollowThisWriter(String writerId, String fanId);

	/**
	 * 关注作家，成为粉丝
	 */
	void follow(String writerId, String fanId);

	/**
	 * 取消关注
	 */
	void unfollow(String writerId, String fanId);

	/**
	 * 查询我的粉丝列表
	 */
	PagedGridResult queryMyFansList(String writerId, Integer page, Integer pageSize);

	/**
	 * 查询男粉丝或者女粉丝数量
	 */
	 Integer queryFansCounts(String writerId, Sex sex);

	/**
	 * 查询每个地域的粉丝数量
	 */
	List<RegionRatioVO> queryRatioByRegion(String writerId);


}
