package com.dongruan.article.service.impl;

import com.dongruan.api.service.BaseService;
import com.dongruan.article.mapper.ArticleMapper;
import com.dongruan.article.mapper.ArticleMapperCustom;
import com.dongruan.article.service.ArticlePortalService;
import com.dongruan.article.service.ArticleService;
import com.dongruan.enums.ArticleAppointType;
import com.dongruan.enums.ArticleReviewLevel;
import com.dongruan.enums.ArticleReviewStatus;
import com.dongruan.enums.YesOrNo;
import com.dongruan.exception.GraceException;
import com.dongruan.grace.result.ResponseStatusEnum;
import com.dongruan.pojo.Article;
import com.dongruan.pojo.Category;
import com.dongruan.pojo.bo.NewArticleBO;
import com.dongruan.pojo.vo.ArticleDetailVO;
import com.dongruan.utils.PagedGridResult;
import com.dongruan.utils.extend.AliTextReviewUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author zhu
 * @date 2022/1/30 23:57:53
 * @description
 */
@Service
public class ArticlePortalServiceImpl extends BaseService implements ArticlePortalService {


	@Autowired
	private ArticleMapper articleMapper;


	@Override
	public PagedGridResult queryIndexMyArticleList(String keyword, Integer category, Integer page, Integer pageSize) {

		Example articleExample = new Example(Article.class);
		Example.Criteria criteria = setDefaultArticleExampleCriteria(articleExample);

		if (StringUtils.isNotBlank(keyword)) {
			criteria.andLike("title", "%" + keyword + "%");
		}

		if (category != null) {
			criteria.andEqualTo("categoryId", category);
		}
		PageHelper.startPage(page, pageSize);
		List<Article> list = articleMapper.selectByExample(articleExample);


		return setterPageGrid(list, page);
	}

	@Override
	public List<Article> queryHotArticleList() {

		Example articleExample = new Example(Article.class);
		Example.Criteria criteria = setDefaultArticleExampleCriteria(articleExample);

		PageHelper.startPage(1, 5);
		List<Article> list = articleMapper.selectByExample(articleExample);

		return list;
	}

	@Override
	public PagedGridResult queryArticleListOfWriter(String writerId, Integer page, Integer pageSize) {
		Example articleExample = new Example(Article.class);

		Example.Criteria criteria = setDefaultArticleExampleCriteria(articleExample);
		criteria.andEqualTo("publishUserId", writerId);

		/**
		 * page: ?????????
		 * pageSize: ??????????????????
		 */
		PageHelper.startPage(page, pageSize);
		List<Article> list = articleMapper.selectByExample(articleExample);
		return setterPageGrid(list, page);
	}

	@Override
	public PagedGridResult queryGoodArticleListOfWriter(String writerId) {
		Example articleExample = new Example(Article.class);
		articleExample.orderBy("publishTime").desc();

		Example.Criteria criteria = setDefaultArticleExampleCriteria(articleExample);
		criteria.andEqualTo("publishUserId", writerId);

		/**
		 * page: ?????????
		 * pageSize: ??????????????????
		 */
		PageHelper.startPage(1, 5);
		List<Article> list = articleMapper.selectByExample(articleExample);
		return setterPageGrid(list, 1);
	}

	@Override
	public ArticleDetailVO queryDetail(String articleId) {

		Article article = new Article();
		article.setId(articleId);
		article.setIsDelete(YesOrNo.NO.type);
		article.setIsAppoint(YesOrNo.NO.type);
		article.setArticleStatus(ArticleReviewStatus.SUCCESS.type);

		Article result = articleMapper.selectOne(article);

		ArticleDetailVO articleDetailVO = new ArticleDetailVO();
		BeanUtils.copyProperties(result, articleDetailVO);

		articleDetailVO.setCover(result.getArticleCover());


		return articleDetailVO;
	}

	/**
	 * ???????????????????????????
	 *
	 * @param articleExample
	 * @return
	 */
	private Example.Criteria setDefaultArticleExampleCriteria(Example articleExample) {
		articleExample.orderBy("publishTime").desc();

		/**
		 * ?????????????????????
		 * isPoint?????????????????????????????????????????????????????????????????????????????????
		 * isDelete?????????????????????????????????????????????????????????
		 * status??????????????????????????????????????????/????????????
		 */
		Example.Criteria criteria = articleExample.createCriteria();
		criteria.andEqualTo("isAppoint", YesOrNo.NO.type);
		criteria.andEqualTo("isDelete", YesOrNo.NO.type);
		criteria.andEqualTo("articleStatus", ArticleReviewStatus.SUCCESS.type);

		return criteria;
	}



}
