package com.dongruan.api.controller.article;


import com.dongruan.grace.result.GraceJSONResult;
import com.dongruan.pojo.bo.NewArticleBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

/**
 * @author zhu
 * @date 2022/1/16 17:12:50
 * @description
 */
@Api(value = "门户端文章业务的controller", tags = {"门户端文章业务的controller"})
@RequestMapping("portal/article")
public interface ArticlePortalControllerApi {


	@ApiOperation(value = "首页查询文章列表", notes = "首页查询文章列表", httpMethod = "GET")
	@GetMapping("/list")
	GraceJSONResult list(@RequestParam String keyword,
	                     @RequestParam Integer category,
	                     @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
	                     @RequestParam Integer page,
	                     @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
	                     @RequestParam Integer pageSize);

	@ApiOperation(value = "首页查询热闻列表", notes = "首页查询热闻列表", httpMethod = "GET")
	@GetMapping("/hotList")
	GraceJSONResult hotList();

	@ApiOperation(value = "查询作家发布的所有文章列表", notes = "查询作家发布的所有文章列表", httpMethod = "GET")
	@GetMapping("/queryArticleListOfWriter")
	GraceJSONResult queryArticleListOfWriter(@RequestParam String writerId,
	                                                @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
	                                                @RequestParam Integer page,
	                                                @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
	                                                @RequestParam Integer pageSize);

	@ApiOperation(value = "作家页面查询近期佳文", notes = "作家页面查询近期佳文", httpMethod = "GET")
	@GetMapping("/queryGoodArticleListOfWriter")
	GraceJSONResult queryGoodArticleListOfWriter(@RequestParam String writerId);


	@ApiOperation(value = "首页查询文章详情", notes = "首页查询文章详情", httpMethod = "GET")
	@GetMapping("/detail")
	GraceJSONResult detail(@RequestParam String articleId);

	@ApiOperation(value = "阅读文章，累加阅读量", notes = "阅读文章，累加阅读量", httpMethod = "POST")
	@PostMapping("/readArticle")
	GraceJSONResult readArticle(@RequestParam String articleId, HttpServletRequest request);

	@ApiOperation(value = "获得文章阅读量", notes = "获得文章阅读量", httpMethod = "GET")
	@GetMapping("/readCounts")
	Integer readCounts(@RequestParam String articleId);


}
