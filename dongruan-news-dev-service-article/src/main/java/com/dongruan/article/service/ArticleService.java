package com.dongruan.article.service;

import com.dongruan.pojo.Article;
import com.dongruan.pojo.Category;
import com.dongruan.pojo.bo.NewArticleBO;
import com.dongruan.utils.PagedGridResult;

import java.util.Date;

/**
 * @author zhu
 * @date 2022/1/30 23:56:30
 * @description
 */
public interface ArticleService {

	/**
	 * 发布文章
	 */
	void createArticle(NewArticleBO newArticleBO, Category category);

	/**
	 * 更新定时发布为即时发布
	 */
	void updateAppointToPublish();

	/**
	 * 用户中心 - 查询我的文章列表
	 */
	PagedGridResult queryMyArticleList(String userId, String keyword, Integer status, Date startDate,
	                                   Date endDate, Integer page, Integer pageSize);

	/**
	 * 更改文章的状态
	 */
	void updateArticleStatus(String articleId, Integer pendingStatus);

	/**
	 * 更新单条文章为即时发布
	 */
	void updateArticleAppointToPublish(String articleId);

	/**
	 * 管理员查询文章列表
	 */
	PagedGridResult queryAllArticleListAdmin(Integer status, Integer page, Integer pageSize);

	/**
	 * 删除文章
	 */
	void deleteArticle(String userId, String articleId);

	/**
	 * 撤回文章
	 */
	void withdrawArticle(String userId, String articleId);

	/**
	 * 关联文章和gridfs的html文件id
	 */
	void updateArticleToGridFS(String articleId, String articleMongoId);


}
