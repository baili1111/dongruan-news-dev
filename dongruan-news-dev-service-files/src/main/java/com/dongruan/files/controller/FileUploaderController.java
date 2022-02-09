package com.dongruan.files.controller;

import com.dongruan.api.controller.files.FileUploaderControllerApi;
import com.dongruan.constant.SystemConstant;
import com.dongruan.exception.GraceException;
import com.dongruan.files.resource.FileResource;
import com.dongruan.files.service.UploaderService;
import com.dongruan.grace.result.GraceJSONResult;
import com.dongruan.grace.result.ResponseStatusEnum;
import com.dongruan.pojo.bo.NewAdminBO;
import com.dongruan.utils.FileUtils;
import com.dongruan.utils.extend.AliImageReviewUtils;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;

/**
 * @author zhu
 * @date 2022/1/16 17:12:50
 * @description
 */
@RestController
public class FileUploaderController implements FileUploaderControllerApi {

	final static Logger logger = LoggerFactory.getLogger(FileUploaderController.class);

	@Autowired
	private UploaderService uploaderService;

	@Autowired
	private FileResource fileResource;

	@Autowired
	private AliImageReviewUtils aliImageReviewUtils;

	@Autowired
	private GridFSBucket gridFSBucket;


	@Override
	public GraceJSONResult uploadFace(String userId, MultipartFile file) throws Exception {

		String path = "";
		if (file != null) {
			// 获得文件上传的名称
			String fileName = file.getOriginalFilename();

			// 判断文件名不能为空
			if (StringUtils.isNotBlank(fileName)) {
				String[] fileNameArr = fileName.split("\\.");
				// 获得后缀
				String suffix = fileNameArr[fileNameArr.length - 1];
				// 判断后缀符合我们的预定义规范
				if (!suffix.equalsIgnoreCase("png") &&
						!suffix.equalsIgnoreCase("jpg") &&
						!suffix.equalsIgnoreCase("jpeg")) {

					return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
				}

				// 执行上传
				//dongruan/M00/00/00/wKhAhWHteuyASFaUAAD0sG1aQ5Q05.jpeg
				//path = uploaderService.uploadFdfs(file, suffix);
				path = uploaderService.uploadOSS(file, userId, suffix);

			} else {
				return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
			}

		} else {
			return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
		}

		//logger.info("path = " + path);

		String finalPath = "";
		if (StringUtils.isNotBlank(path)) {
			//finalPath = fileResource.getHost() + path;
			finalPath = fileResource.getOssHost() + path;
		} else {
			return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
		}

		return GraceJSONResult.ok(finalPath);
		// 用aliyun提供的图片审核进行审核
		//return GraceJSONResult.ok(doAliImageReview(finalPath));
	}

	@Override
	public GraceJSONResult uploadSomeFiles(String userId, MultipartFile[] files) throws Exception {

		// 声明List，用于存放多个图片的地址路径，返回到前端
		ArrayList<Object> imageUrlList = new ArrayList<>();
		if (files != null && files.length > 0) {
			for (MultipartFile file : files) {
				String path = "";
				if (file != null) {
					// 获得文件上传的名称
					String fileName = file.getOriginalFilename();

					// 判断文件名不能为空
					if (StringUtils.isNotBlank(fileName)) {
						String[] fileNameArr = fileName.split("\\.");
						// 获得后缀
						String suffix = fileNameArr[fileNameArr.length - 1];
						// 判断后缀符合我们的预定义规范
						if (!suffix.equalsIgnoreCase("png") &&
								!suffix.equalsIgnoreCase("jpg") &&
								!suffix.equalsIgnoreCase("jpeg")) {
							continue;
						}

						// 执行上传
						//dongruan/M00/00/00/wKhAhWHteuyASFaUAAD0sG1aQ5Q05.jpeg
						//path = uploaderService.uploadFdfs(file, suffix);
						path = uploaderService.uploadOSS(file, userId, suffix);

					} else {
						continue;
					}

				} else {
					continue;
				}

				logger.info("path = " + path);

				String finalPath = "";
				if (StringUtils.isNotBlank(path)) {
					//finalPath = fileResource.getHost() + path;
					finalPath = fileResource.getOssHost() + path;
					// FIXME: 放入到imageList之前，需要对图片做一次审核
					imageUrlList.add(finalPath);
				} else {
					continue;
				}

			}

		}

		return GraceJSONResult.ok(imageUrlList);
	}

	@Override
	public GraceJSONResult uploadToGridFS(NewAdminBO newAdminBO) throws Exception {

		// 获得图片的base64字符串
		String file64 = newAdminBO.getImg64();

		// 将base64字符串转换为byte数组
		byte[] bytes = new BASE64Decoder().decodeBuffer(file64.trim());

		// 转换为输入流
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

		// 上传到gridfs中
		ObjectId fileId = gridFSBucket.uploadFromStream(newAdminBO.getUsername() + ".png", byteArrayInputStream);

		// 获得文件在gridfs中的主键id
		String fileIdStr = fileId.toString();

		return GraceJSONResult.ok(fileIdStr);
	}

	@Override
	public void readInGridFS(String faceId,
	                         HttpServletRequest request,
	                         HttpServletResponse response) throws Exception {

		// 0.判断参数
		if (StringUtils.isBlank(faceId) || faceId.equalsIgnoreCase("null")) {
			GraceException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
		}

		// 1.从gridfs中读取
		File adminFace = readGridFSByFaceId(faceId);

		// 2.把人脸图片输出到浏览器
		FileUtils.downloadFileByStream(response, adminFace);

	}

	@Override
	public GraceJSONResult readFace64InGridFS(String faceId,
	                                          HttpServletRequest request,
	                                          HttpServletResponse response) throws Exception {
		// 1. 判断faceId不能为空
		if (StringUtils.isBlank(faceId)) {
			return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
		}

		// 2. 获得文件
		File file = readGridFSByFaceId(faceId);

		// 3. 把文件转换为base64并返回给调用方
		String base64 = FileUtils.fileToBase64(file);

		return GraceJSONResult.ok(base64);
	}


	private File readGridFSByFaceId(String faceId) throws Exception {

		GridFSFindIterable gridFSFiles = gridFSBucket.find(Filters.eq("_id", new ObjectId(faceId)));

		GridFSFile gridFSFile = gridFSFiles.first();

		if (gridFSFile == null) {
			GraceException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
		}

		String fileName = gridFSFile.getFilename();
		System.out.println(gridFSFile.getFilename());

		// 获取文件流，保存文件到本地或者服务器的临时目录
		File fileTemp = new File("/workspace/temp_face");
		if (!fileTemp.exists()) {
			fileTemp.mkdirs();
		}

		File myFile = new File("/workspace/temp_face/" + fileName);

		// 创建文件输出流
		OutputStream os = new FileOutputStream(myFile);

		// 下载到服务器或者本地
		gridFSBucket.downloadToStream(new ObjectId(faceId), os);

		return myFile;
	}

	/**
	 * aliyun图片审核
	 *
	 * @param pendingImageUrl
	 * @return
	 */
	private String doAliImageReview(String pendingImageUrl) {

		boolean result = false;

		try {
			result = aliImageReviewUtils.reviewImage(pendingImageUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!result) {
			return SystemConstant.FAILED_IMAGE_URL;
		}

		return pendingImageUrl;

	}

}
