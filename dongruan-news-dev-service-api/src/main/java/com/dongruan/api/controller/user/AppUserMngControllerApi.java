package com.dongruan.api.controller.user;


import com.dongruan.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * @author zhu
 * @date 2022/1/16 17:12:50
 * @description
 */
@Api(value = "用户管理相关的接口定义", tags = {"用户管理相关的接口定义"})
@RequestMapping("appUser")
public interface AppUserMngControllerApi {

	@ApiOperation(value = "查询所有网站用户", notes = "查询所有网站用户", httpMethod = "GET")
	@PostMapping("/queryAll")
	public GraceJSONResult queryAll(@RequestParam String nickname,
	                                @RequestParam Integer status,
	                                @RequestParam Date startDate,
	                                @RequestParam Date endDate,
	                                @RequestParam Integer page,
	                                @RequestParam Integer pageSize);


	@ApiOperation(value = "查看用户详情信息", notes = "查看用户详情信息", httpMethod = "POST")
	@PostMapping("/userDetail")
	public GraceJSONResult userDetail(@RequestParam String userId);

	@ApiOperation(value = "冻结用户，或解除封号", notes = "冻结用户，或解除封号", httpMethod = "POST")
	@PostMapping("/freezeUserOrNot")
	public GraceJSONResult freezeUserOrNot(@RequestParam String userId, @RequestParam Integer doStatus);


}
