package com.dongruan.api.controller.user;

import com.dongruan.grace.result.GraceJSONResult;
import com.dongruan.pojo.bo.RegistLoginBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author zhu
 * @date 2022/1/19 00:05:03
 * @description
 */
@Api(value = "用户注册登录", tags = {"用户注册登录的controller"})
@RequestMapping("passport")
public interface PassportControllerApi {

	@ApiOperation(value = "获得短信验证码", notes = "获得短信验证码", httpMethod = "GET")
	@GetMapping("/getSMSCode")
	 GraceJSONResult getSMSCode(@RequestParam String mobile, HttpServletRequest request);

	//@ApiOperation(value = "一键注册登录接口", notes = "一键注册登录接口", httpMethod = "POST")
	//@PostMapping("/doLogin")
	//GraceJSONResult doLogin(@RequestBody @Valid RegistLoginBO registLoginBo,
	//                               BindingResult result,
	//                               HttpServletRequest request,
	//                               HttpServletResponse response);

	@ApiOperation(value = "一键注册登录接口", notes = "一键注册登录接口", httpMethod = "POST")
	@PostMapping("/doLogin")
	GraceJSONResult doLogin(@RequestBody @Valid RegistLoginBO registLoginBo,
	                               HttpServletRequest request,
	                               HttpServletResponse response);

	@ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
	@PostMapping("/logout")
	GraceJSONResult logout(@RequestParam String userId,
	                               HttpServletRequest request,
	                               HttpServletResponse response);



}
