package com.dongruan.api.controller.article;


import com.dongruan.grace.result.GraceJSONResult;
import com.dongruan.pojo.bo.CommentReplyBO;
import com.dongruan.pojo.bo.NewArticleBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * @author zhu
 * @date 2022/1/16 17:12:50
 * @description
 */
@Api(value = "文章详情页的评论业务", tags = {"文章详情页的评论业务controller"})
@RequestMapping("comment")
public interface CommentControllerApi {

	//@ApiOperation(value = "用户留言，或回复留言", notes = "用户留言，或回复留言", httpMethod = "POST")
	//@PostMapping("/createComment")
	//GraceJSONResult createComment(@RequestBody @Valid CommentReplyBO commentReplyBO, BindingResult result);

	@ApiOperation(value = "用户留言，或回复留言", notes = "用户留言，或回复留言", httpMethod = "POST")
	@PostMapping("/createComment")
	GraceJSONResult createComment(@RequestBody @Valid CommentReplyBO commentReplyBO);

	@ApiOperation(value = "用户评论数查询", notes = "用户评论数查询", httpMethod = "GET")
	@GetMapping("/counts")
	GraceJSONResult commentCounts(@RequestParam String articleId);

	@ApiOperation(value = "查询某文章的所有评论列表", notes = "查询某文章的所有评论列表", httpMethod = "GET")
	@GetMapping("/list")
	GraceJSONResult list(@RequestParam String articleId,
	                     @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
	                     @RequestParam Integer page,
	                     @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
	                     @RequestParam Integer pageSize);

	@ApiOperation(value = "查询我的评论管理列表", notes = "查询我的评论管理列表", httpMethod = "POST")
	@PostMapping("/mng")
	GraceJSONResult mng(@RequestParam String writerId,
	                    @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
	                    @RequestParam Integer page,
	                    @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
	                    @RequestParam Integer pageSize);


	@ApiOperation(value = "作者删除评论", notes = "作者删除评论", httpMethod = "POST")
	@PostMapping("/delete")
	GraceJSONResult delete(@RequestParam String writerId,
	                       @RequestParam String commentId);


}
