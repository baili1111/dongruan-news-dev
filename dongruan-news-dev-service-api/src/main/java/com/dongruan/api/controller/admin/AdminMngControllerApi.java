package com.dongruan.api.controller.admin;


import com.dongruan.grace.result.GraceJSONResult;
import com.dongruan.pojo.bo.AdminLoginBO;
import com.dongruan.pojo.bo.NewAdminBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhu
 * @date 2022/1/16 17:12:50
 * @description
 */
@Api(value = "管理员admin维护", tags = {"管理员admin维护"})
@RequestMapping("adminMng")
public interface AdminMngControllerApi {


	@ApiOperation(value = "管理员登录接口", notes = "管理员登录接口", httpMethod = "POST")
	@PostMapping("/adminLogin")
	public GraceJSONResult adminLogin(@RequestBody AdminLoginBO adminLoginBO,
	                                  HttpServletRequest request,
	                                  HttpServletResponse response);


	@ApiOperation(value = "查询admin用户名是否存在", notes = "查询admin用户名是否存在", httpMethod = "POST")
	@PostMapping("/adminIsExist")
	public GraceJSONResult adminIsExist(@RequestParam String username);


	@ApiOperation(value = "创建admin", notes = "创建admin", httpMethod = "POST")
	@PostMapping("/addNewAdmin")
	public GraceJSONResult addNewAdmin(@RequestBody NewAdminBO newAdminBO,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response);

	@ApiOperation(value = "查询admin列表", notes = "查询admin列表", httpMethod = "POST")
	@PostMapping("/getAdminList")
	public GraceJSONResult getAdminList(@ApiParam(name = "page", value = "查询下一页的第几页", required = false)
	                                    @RequestParam Integer page,
	                                    @ApiParam(name = "pageSize", value = "分页的每一页条数", required = false)
	                                    @RequestParam Integer pageSize);

	@PostMapping("/adminLogout")
	@ApiOperation(value = "admin退出登录", notes = "admin退出登录", httpMethod = "POST")
	public GraceJSONResult adminLogout(@RequestParam String adminId,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response);

	@PostMapping("/adminFaceLogin")
	@ApiOperation(value = "admin管理员人脸登录", notes = "admin管理员人脸登录", httpMethod = "POST")
	public GraceJSONResult adminFaceLogin(@RequestBody AdminLoginBO adminLoginBO,
	                                      HttpServletRequest request,
	                                      HttpServletResponse response);

}
