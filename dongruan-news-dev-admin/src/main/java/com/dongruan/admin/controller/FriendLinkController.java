package com.dongruan.admin.controller;

import com.dongruan.admin.service.FriendLinkService;
import com.dongruan.api.controller.BaseController;
import com.dongruan.api.controller.admin.FriendLinkControllerApi;
import com.dongruan.grace.result.GraceJSONResult;
import com.dongruan.pojo.bo.SaveFriendLinkBO;
import com.dongruan.pojo.mo.FriendLinkMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhu
 * @date 2022/1/16 17:12:50
 * @description
 */
@RestController
public class FriendLinkController extends BaseController implements FriendLinkControllerApi {

	final static Logger logger = LoggerFactory.getLogger(FriendLinkController.class);

	@Autowired
	private FriendLinkService friendLinkService;

	@Override
	public GraceJSONResult saveOrUpdateFriendLink(@Valid SaveFriendLinkBO saveFriendLinkBO) {
		//	, BindingResult result) {
		//
		//// 校验数据
		//if (result.hasErrors()) {
		//	Map<String, String> map = getErrors(result);
		//	return GraceJSONResult.errorMap(map);
		//}

		FriendLinkMO saveFriendLinkMO = new FriendLinkMO();
		BeanUtils.copyProperties(saveFriendLinkBO, saveFriendLinkMO);
		saveFriendLinkMO.setCreateTime(new Date());
		saveFriendLinkMO.setUpdateTime(new Date());


		friendLinkService.saveOrUpdateFriendLink(saveFriendLinkMO);

		return GraceJSONResult.ok();
	}

	@Override
	public GraceJSONResult getFriendLinkList() {
		List<FriendLinkMO> friendLinkMOS = friendLinkService.queryAllFriendLinkList();
		return GraceJSONResult.ok(friendLinkMOS);
	}

	@Override
	public GraceJSONResult delete(String linkId) {

		friendLinkService.delete(linkId);
		return GraceJSONResult.ok();
	}

	@Override
	public GraceJSONResult queryPortalFriendLinkList() {
		List<FriendLinkMO> list = friendLinkService.queryPortalFriendLinkList();
		return GraceJSONResult.ok(list);
	}
}
