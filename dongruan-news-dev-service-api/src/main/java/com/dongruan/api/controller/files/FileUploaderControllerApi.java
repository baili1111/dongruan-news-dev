package com.dongruan.api.controller.files;


import com.dongruan.grace.result.GraceJSONResult;
import com.dongruan.pojo.bo.AdminLoginBO;
import com.dongruan.pojo.bo.NewAdminBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhu
 * @date 2022/1/16 17:12:50
 * @description
 */
@Api(value = "文件上传的controller", tags = {"文件上传的controller"})
@RequestMapping("fs")
public interface FileUploaderControllerApi {

	/**
	 * 上传单文件
	 * @param userId
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/uploadFace")
	public GraceJSONResult uploadFace(@RequestParam String userId, MultipartFile file) throws Exception;


	/**
	 * 上传多个文件
 	 * @param userId
	 * @param files
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/uploadSomeFiles")
	public GraceJSONResult uploadSomeFiles(@RequestParam String userId, MultipartFile[] files) throws Exception;

	/**
	 * 文件上传到mongodb的gridfs中
	 *
	 * @param newAdminBO
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/uploadToGridFS")
	public GraceJSONResult uploadToGridFS(@RequestBody NewAdminBO newAdminBO) throws Exception;


	/**
	 * 从gridfs中读取图片内容
	 *
	 * @param faceId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/readInGridFS")
	public void readInGridFS(String faceId,
	                         HttpServletRequest request,
	                         HttpServletResponse response) throws Exception;

	/**
	 * 根据faceId获得admin的base64头像信息
	 * @param faceId
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/readFace64InGridFS")
	public GraceJSONResult readFace64InGridFS(
			@RequestParam String faceId,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception;



}
