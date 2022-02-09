package com.dongruan.api.controller.user;

import com.dongruan.api.controller.user.fallbacks.UserControllerFactoryFallBack;
import com.dongruan.constant.SystemConstant;
import com.dongruan.grace.result.GraceJSONResult;
import com.dongruan.pojo.bo.UpdateUserInfoBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author zhu
 * @date 2022/1/19 00:05:03
 * @description
 */
@Api(value = "用户信息相关Controller", tags = {"用户信息相关Controller"})
@RequestMapping("user")
@FeignClient(value = SystemConstant.SERVICE_USER, fallbackFactory = UserControllerFactoryFallBack.class)
public interface UserControllerApi {


	@ApiOperation(value = "获得用户基本信息", notes = "获得用户基本信息", httpMethod = "POST")
	@PostMapping("/getUserInfo")
	GraceJSONResult getUserInfo(@RequestParam String userId);

	@ApiOperation(value = "获得用户账户信息", notes = "获得用户账户信息", httpMethod = "POST")
	@PostMapping("/getAccountInfo")
	GraceJSONResult getAccountInfo(@RequestParam String userId);


	//@ApiOperation(value = "修改/完善账户信息", notes = "修改/完善账户信息", httpMethod = "POST")
	//@PostMapping("/updateUserInfo")
	//GraceJSONResult updateUserInfo(@RequestBody @Valid UpdateUserInfoBO updateUserInfoBO,
	//                               @RequestParam BindingResult result);

	@ApiOperation(value = "修改/完善账户信息", notes = "修改/完善账户信息", httpMethod = "POST")
	@PostMapping("/updateUserInfo")
	GraceJSONResult updateUserInfo(@RequestBody @Valid UpdateUserInfoBO updateUserInfoBO);

	@ApiOperation(value = "根据用户id查询用户", notes = "根据用户id查询用户", httpMethod = "GET")
	@GetMapping("/queryByIds")
	GraceJSONResult queryByIds(@RequestParam String userIds);


}
