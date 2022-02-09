package com.dongruan.user.controller;

import com.dongruan.api.controller.BaseController;
import com.dongruan.api.controller.user.MyFansControllerApi;
import com.dongruan.constant.SystemConstant;
import com.dongruan.enums.Sex;
import com.dongruan.grace.result.GraceJSONResult;
import com.dongruan.pojo.vo.FansCountsVO;
import com.dongruan.pojo.vo.RegionRatioVO;
import com.dongruan.user.service.MyFansService;
import com.dongruan.utils.PagedGridResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhu
 * @date 2022/1/16 17:12:50
 * @description
 */
@RestController
public class MyFansController extends BaseController implements MyFansControllerApi {

	final static Logger logger = LoggerFactory.getLogger(MyFansController.class);

	@Autowired
	private MyFansService myFansService;

	@Override
	public GraceJSONResult isMeFollowThisWriter(String writerId, String fanId) {
		// 判空

		boolean res = myFansService.isMeFollowThisWriter(writerId, fanId);
		return GraceJSONResult.ok(res);
	}

	@Override
	public GraceJSONResult follow(String writerId, String fanId) {
		// 判空

		myFansService.follow(writerId, fanId);
		return GraceJSONResult.ok();
	}

	@Override
	public GraceJSONResult unfollow(String writerId, String fanId) {
		// 判空

		myFansService.unfollow(writerId, fanId);
		return GraceJSONResult.ok();
	}

	@Override
	public GraceJSONResult queryAll(String writerId, Integer page, Integer pageSize) {
		// 判空

		if (page == null) {
			page = SystemConstant.COMMON_START_PAGE;
		}

		if (pageSize == null) {
			pageSize = SystemConstant.COMMON_PAGE_SIZE;
		}

		PagedGridResult pagedGridResult = myFansService.queryMyFansList(writerId, page, pageSize);

		return GraceJSONResult.ok(pagedGridResult);
	}

	@Override
	public GraceJSONResult queryRatio(String writerId) {
		Integer manCounts = myFansService.queryFansCounts(writerId, Sex.man);
		Integer womanCounts = myFansService.queryFansCounts(writerId, Sex.woman);

		FansCountsVO fansCountsVO = new FansCountsVO();
		fansCountsVO.setManCounts(manCounts);
		fansCountsVO.setWomanCounts(womanCounts);

		return GraceJSONResult.ok(fansCountsVO);
	}

	@Override
	public GraceJSONResult queryRatioByRegion(String writerId) {

		List<RegionRatioVO> regionRatioVOS = myFansService.queryRatioByRegion(writerId);
		return GraceJSONResult.ok(regionRatioVOS);
	}

}
