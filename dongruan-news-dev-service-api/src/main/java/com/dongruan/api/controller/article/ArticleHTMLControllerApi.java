package com.dongruan.api.controller.article;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhu
 * @date 2022/2/6 13:21:14
 * @description
 */
@Api(value = "静态化文章业务的controller", tags = {"静态化文章业务的controller"})
@RequestMapping("article/html")
public interface ArticleHTMLControllerApi {

	@ApiOperation(value = "下载文章html", notes = "下载文章html", httpMethod = "GET")
	@GetMapping("/download")
	Integer download(String articleId, String articleMongoId) throws Exception;

	@ApiOperation(value = "删除html", notes = "删除html", httpMethod = "GET")
	@GetMapping("/delete")
	Integer delete(String articleId) throws Exception;

}
