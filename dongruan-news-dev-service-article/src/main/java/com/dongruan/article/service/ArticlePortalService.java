package com.dongruan.article.service;

import com.dongruan.pojo.Article;
import com.dongruan.pojo.Category;
import com.dongruan.pojo.bo.NewArticleBO;
import com.dongruan.pojo.vo.ArticleDetailVO;
import com.dongruan.utils.PagedGridResult;

import java.util.Date;
import java.util.List;

/**
 * @author zhu
 * @date 2022/1/30 23:56:30
 * @description
 */
public interface ArticlePortalService {

	/**
	 * 首页查询文章列表
	 */
	PagedGridResult queryIndexMyArticleList(String keyword, Integer category, Integer page, Integer pageSize);


	/**
	 * 首页查询热闻列表
	 */
	List<Article> queryHotArticleList();

	/**
	 * 查询作家发布的所有文章列表
	 */
	PagedGridResult queryArticleListOfWriter(String writerId,
	                                         Integer page,
	                                         Integer pageSize);

	/**
	 * 作家页面查询近期佳文
	 */
	PagedGridResult queryGoodArticleListOfWriter(String writerId);

	/**
	 * 文章详情
	 */
	ArticleDetailVO queryDetail(String articleId);

}
