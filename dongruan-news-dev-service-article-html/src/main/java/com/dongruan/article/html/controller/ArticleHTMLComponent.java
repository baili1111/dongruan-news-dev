package com.dongruan.article.html.controller;

import com.dongruan.api.controller.article.ArticleHTMLControllerApi;
import com.mongodb.client.gridfs.GridFSBucket;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author zhu
 * @date 2022/1/16 17:12:50
 * @description
 */
@Component
public class ArticleHTMLComponent {


	@Autowired
	private GridFSBucket gridFSBucket;

	@Value("${freemarker.html.article}")
	private String articlePath;

	public Integer download(String articleId, String articleMongoId) throws Exception {

		// 拼接最终文件的保存的地址
		String path = articlePath + File.separator + articleId + ".html";

		// 获得文件流，定义存放的位置和名称
		File file = new File(path);

		// 创建输出流
		FileOutputStream outputStream = new FileOutputStream(file);

		// 执行下载
		gridFSBucket.downloadToStream(new ObjectId(articleMongoId), outputStream);



		return HttpStatus.OK.value();
	}

	public Integer delete(String articleId) throws Exception {

		// 拼接最终文件的保存的地址
		String path = articlePath + File.separator + articleId + ".html";

		// 获取文件流，定义存放的位置和名称
		File file = new File(path);

		// 删除文件
		file.delete();

		return HttpStatus.OK.value();
	}
}
