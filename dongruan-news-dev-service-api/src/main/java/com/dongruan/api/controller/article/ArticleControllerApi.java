package com.dongruan.api.controller.article;


import com.dongruan.grace.result.GraceJSONResult;
import com.dongruan.pojo.bo.NewArticleBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;

/**
 * @author zhu
 * @date 2022/1/16 17:12:50
 * @description
 */
@Api(value = "文章业务的controller", tags = {"文章业务的controller"})
@RequestMapping("article")
public interface ArticleControllerApi {


	//@ApiOperation(value = "用户发文", notes = "用户发文", httpMethod = "POST")
	//@PostMapping("/createArticle")
	//GraceJSONResult createArticle(@RequestBody @Valid NewArticleBO newArticleBO, BindingResult result);

	@ApiOperation(value = "用户发文", notes = "用户发文", httpMethod = "POST")
	@PostMapping("/createArticle")
	GraceJSONResult createArticle(@RequestBody @Valid NewArticleBO newArticleBO);


	@ApiOperation(value = "查询用户的所有文章列表", notes = "查询用户的所有文章列表", httpMethod = "POST")
	@PostMapping("/queryMyList")
	GraceJSONResult queryMyList(@RequestParam String userId,
	                            @RequestParam String keyword,
	                            @RequestParam Integer status,
	                            @RequestParam Date startDate,
	                            @RequestParam Date endDate,
	                            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
	                            @RequestParam Integer page,
	                            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
	                            @RequestParam Integer pageSize);


	@ApiOperation(value = "管理员查询用户的所有文章列表", notes = "管理员查询用户的所有文章列表", httpMethod = "POST")
	@PostMapping("/queryAllList")
	GraceJSONResult queryAllList(@RequestParam Integer status,
	                             @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
	                             @RequestParam Integer page,
	                             @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
	                             @RequestParam Integer pageSize);

	@ApiOperation(value = "管理员对文章进行审核通过或者失败", notes = "管理员对文章进行审核通过或者失败", httpMethod = "POST")
	@PostMapping("/doReview")
	GraceJSONResult doReview(@RequestParam String articleId, @RequestParam Integer passOrNot);

	@ApiOperation(value = "用户删除文章", notes = "用户删除文章", httpMethod = "POST")
	@PostMapping("/delete")
	GraceJSONResult delete(@RequestParam String userId,
	                       @RequestParam String articleId);

	@ApiOperation(value = "用户撤回文章", notes = "用户撤回文章", httpMethod = "POST")
	@PostMapping("/withdraw")
	GraceJSONResult withdraw(@RequestParam String userId,
	                         @RequestParam String articleId);
}
