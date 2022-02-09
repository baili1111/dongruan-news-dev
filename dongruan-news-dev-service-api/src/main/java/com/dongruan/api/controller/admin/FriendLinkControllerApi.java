package com.dongruan.api.controller.admin;

import com.dongruan.grace.result.GraceJSONResult;
import com.dongruan.pojo.bo.SaveFriendLinkBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author zhu
 * @date 2022/1/16 17:12:50
 * @description
 */
@Api(value = "首页友情链接维护", tags = {"首页友情链接维护"})
@RequestMapping("friendLinkMng")
public interface FriendLinkControllerApi {


	//@ApiOperation(value = "新增或者修改友情链接", notes = "新增或者修改友情链接", httpMethod = "POST")
	//@PostMapping("/saveOrUpdateFriendLink")
	//public GraceJSONResult saveOrUpdateFriendLink(@RequestBody @Valid SaveFriendLinkBO saveFriendLinkBO,
	//                                              BindingResult result);

	@ApiOperation(value = "新增或者修改友情链接", notes = "新增或者修改友情链接", httpMethod = "POST")
	@PostMapping("/saveOrUpdateFriendLink")
	GraceJSONResult saveOrUpdateFriendLink(@RequestBody @Valid SaveFriendLinkBO saveFriendLinkBO);


	@ApiOperation(value = "查询友情链接列表", notes = "查询友情链接列表", httpMethod = "POST")
	@PostMapping("/getFriendLinkList")
	GraceJSONResult getFriendLinkList();


	@ApiOperation(value = "删除友情链接", notes = "删除友情链接", httpMethod = "POST")
	@PostMapping("/delete")
	GraceJSONResult delete(@RequestParam String linkId);


	@ApiOperation(value = "门户端查询友情链接", notes = "门户端查询友情链接", httpMethod = "GET")
	@GetMapping("/portal/list")
	GraceJSONResult queryPortalFriendLinkList();





}
