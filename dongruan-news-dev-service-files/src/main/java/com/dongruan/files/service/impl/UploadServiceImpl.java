package com.dongruan.files.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.dongruan.files.resource.FileResource;
import com.dongruan.files.service.UploaderService;
import com.dongruan.utils.extend.AliyunResource;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;


/**
 * @author zhu
 * @date 2022/1/23 23:12:35
 * @description
 */
@Service
public class UploadServiceImpl implements UploaderService {

	@Autowired
	public FastFileStorageClient fastFileStorageClient;

	@Autowired
	public AliyunResource aliyunResource;

	@Autowired
	public FileResource fileResource;

	@Autowired
	public Sid sid;

	@Override
	public String uploadFdfs(MultipartFile file, String fileExtName) throws Exception {

		StorePath storePath = fastFileStorageClient.uploadFile(
				file.getInputStream(),
				file.getSize(),
				fileExtName,
				null);

		return storePath.getFullPath();
	}

	@Override
	public String uploadOSS(MultipartFile file, String userId, String fileExtName) throws Exception {

		// yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
		String endpoint = fileResource.getEndpoint();

		// 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
		String accessKeyId = aliyunResource.getAccessKeyID();
		String accessKeySecret = aliyunResource.getAccessKeySecret();

		// 创建OSSClient实例。
		OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

		String fileName = sid.nextShort();
		String myObjectName = fileResource.getObjectName() + "/" + userId + "/" +fileName + "." + fileExtName;

		// 填写网络流地址。
		//InputStream inputStream = new URL("https://www.aliyun.com/").openStream();
		InputStream inputStream = file.getInputStream();

		// 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
		ossClient.putObject(fileResource.getBucketName(), myObjectName, inputStream);

		// 关闭OSSClient。
		ossClient.shutdown();


		return myObjectName;
	}
}
