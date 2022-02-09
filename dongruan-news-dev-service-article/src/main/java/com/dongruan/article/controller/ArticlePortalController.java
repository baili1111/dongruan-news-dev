package com.dongruan.article.controller;

import com.dongruan.api.controller.BaseController;
import com.dongruan.api.controller.article.ArticleControllerApi;
import com.dongruan.api.controller.article.ArticlePortalControllerApi;
import com.dongruan.api.controller.user.UserControllerApi;
import com.dongruan.article.service.ArticlePortalService;
import com.dongruan.article.service.ArticleService;
import com.dongruan.constant.SystemConstant;
import com.dongruan.enums.ArticleCoverType;
import com.dongruan.enums.ArticleReviewStatus;
import com.dongruan.enums.YesOrNo;
import com.dongruan.grace.result.GraceJSONResult;
import com.dongruan.grace.result.ResponseStatusEnum;
import com.dongruan.pojo.Article;
import com.dongruan.pojo.Category;
import com.dongruan.pojo.bo.NewArticleBO;
import com.dongruan.pojo.vo.AppUserVO;
import com.dongruan.pojo.vo.ArticleDetailVO;
import com.dongruan.pojo.vo.IndexArticleVO;
import com.dongruan.utils.IPUtil;
import com.dongruan.utils.JsonUtils;
import com.dongruan.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * @author zhu
 * @date 2022/1/16 17:12:50
 * @description
 */
@RestController
public class ArticlePortalController extends BaseController implements ArticlePortalControllerApi {

	final static Logger logger = LoggerFactory.getLogger(ArticlePortalController.class);

	@Autowired
	private ArticlePortalService articlePortalService;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public GraceJSONResult list(String keyword, Integer category, Integer page, Integer pageSize) {
		if (page == null) {
			page = SystemConstant.COMMON_START_PAGE;
		}

		if (pageSize == null) {
			pageSize = SystemConstant.COMMON_PAGE_SIZE;
		}

		PagedGridResult gridResult = articlePortalService.queryIndexMyArticleList(keyword, category, page, pageSize);


		// START

		/**
		 * FIXME:
		 * 并发查询的时候要减少多表关联查询，尤其首页的文章列表。
		 * 其次，微服务有边界，不同系统各自需要查询各自的表数据
		 * 在这里采用单表查询文章以及用户，然后再业务层（controller或service）拼接，
		 * 而且，文章服务和用户服务是分开的，所以持久层的查询也是在不同的系统进行调用的。
		 * 对于用户来说是无感知的，这也是比较好的一种方式。
		 * 此外，后续结合elasticsearch扩展也是通过业务层拼接方式来做。
		 */

		gridResult = rebuildArticleGrid(gridResult);


		return GraceJSONResult.ok(gridResult);
	}

	@Override
	public GraceJSONResult hotList() {
		return GraceJSONResult.ok(articlePortalService.queryHotArticleList());
	}


	@Override
	public GraceJSONResult queryArticleListOfWriter(String writerId, Integer page, Integer pageSize) {

		System.out.println("writerId=" + writerId);

		if (page == null) {
			page = SystemConstant.COMMON_START_PAGE;
		}

		if (pageSize == null) {
			pageSize = SystemConstant.COMMON_PAGE_SIZE;
		}

		PagedGridResult gridResult = articlePortalService.queryArticleListOfWriter(writerId, page, pageSize);
		gridResult = rebuildArticleGrid(gridResult);
		return GraceJSONResult.ok(gridResult);
	}

	@Override
	public GraceJSONResult queryGoodArticleListOfWriter(String writerId) {
		PagedGridResult gridResult = articlePortalService.queryGoodArticleListOfWriter(writerId);
		return GraceJSONResult.ok(gridResult);
	}

	@Override
	public GraceJSONResult detail(String articleId) {

		ArticleDetailVO articleDetailVO = articlePortalService.queryDetail(articleId);

		Set<String> idSet = new HashSet<>();
		idSet.add(articleDetailVO.getPublishUserId());
		List<AppUserVO> publisherList = getPublisherList(idSet);

		if (!publisherList.isEmpty()) {
			articleDetailVO.setPublishUserName(publisherList.get(0).getNickname());
		}

		articleDetailVO.setReadCounts(getCountsFromRedis(SystemConstant.REDIS_ARTICLE_COMMENT_COUNTS + ":" + articleId));

		return GraceJSONResult.ok(articleDetailVO);
	}

	@Override
	public GraceJSONResult readArticle(String articleId, HttpServletRequest request) {

		String userIP = IPUtil.getRequestIp(request);
		// 设置永久存在key，表示该ip已经阅读过了，无法累加阅读量
		redis.setnx(SystemConstant.REDIS_ARTICLE_ALREADY_READ + ":" + articleId + ":" + userIP, userIP);


		redis.increment(SystemConstant.REDIS_ARTICLE_COMMENT_COUNTS + ":" + articleId, 1);

		return GraceJSONResult.ok();
	}

	@Override
	public Integer readCounts(String articleId) {
		return getCountsFromRedis(SystemConstant.REDIS_ARTICLE_COMMENT_COUNTS + ":" + articleId);
	}

	/**
	 * 从publisherList中获得匹配的用户
	 *
	 * @param publisherId
	 * @param publisherList
	 * @return
	 */
	private AppUserVO getUserIfEqualPublisher(String publisherId,
	                                          List<AppUserVO> publisherList) {
		for (AppUserVO u : publisherList) {
			if (u.getId().equalsIgnoreCase(publisherId)) {
				return u;
			}
		}
		return null;
	}


	private PagedGridResult rebuildArticleGrid(PagedGridResult gridResult) {
		List<Article> list = (List<Article>) gridResult.getRows();

		// 1.构建发布者id列表
		Set<String> idSet = new HashSet<>();
		List<String> idList = new ArrayList<>();
		for (Article a : list) {
			//System.out.println(a.getPublishUserId());
			// 1.1 构建发布者的set
			idSet.add(a.getPublishUserId());
			// 1.2 构建文章 id 的list
			idList.add(SystemConstant.REDIS_ARTICLE_COMMENT_COUNTS + ":" + a.getId());
		}

		// 发起 redis的meget批量查询api，获得对应的值
		List<String> readCountsRedisList = redis.mget(idList);

		// 2.发起远程调用（restTemplate），请求用户服务获得用户（idSet发布者）的列表
		List<AppUserVO> publisherList = getPublisherList(idSet);

		// 3.拼接两个list，重组文章列表
		List<IndexArticleVO> indexArticleList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			IndexArticleVO indexArticleVO = new IndexArticleVO();
			Article a = list.get(i);
			BeanUtils.copyProperties(a, indexArticleVO);

			// 3.1 从 publisherList 中获得发布者的基本信息
			AppUserVO publisher = getUserIfEqualPublisher(a.getPublishUserId(), publisherList);
			indexArticleVO.setPublisherVO(publisher);

			// 3.2 重新组装设置文章列表中的阅读数
			String redisCountsStr = readCountsRedisList.get(i);
			int readCounts = 0;
			if (StringUtils.isNotBlank(redisCountsStr)) {
				readCounts = Integer.valueOf(redisCountsStr);
			}
			indexArticleVO.setReadCounts(readCounts);


			indexArticleList.add(indexArticleVO);
		}

		gridResult.setRows(indexArticleList);


		return gridResult;
	}

	// 注入服务发现，可以获得已经注册的服务相关信息
	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private UserControllerApi userControllerApi;

	// 发起远程调用，获得用户的基本信息
	private List<AppUserVO> getPublisherList(Set idSet) {

		String serviceId = "SERVICE-USER";
		//List<ServiceInstance> instanceList = discoveryClient.getInstances(serviceId);
		//ServiceInstance serviceInstance = instanceList.get(0);

		String userServerUrlExecute = "http://" + serviceId + "/user/queryByIds?userIds=" + JsonUtils.objectToJson(idSet);

		GraceJSONResult bodyResult = userControllerApi.queryByIds(JsonUtils.objectToJson(idSet));

		//String userServerUrlExecute =
		//		"http://" +
		//				serviceInstance.getHost() +
		//				":" +
		//				serviceInstance.getPort() +
		//				"/user/queryByIds?userIds=" + JsonUtils.objectToJson(idSet);

		//String userServerUrlExecute =
		//		"http://user.dongruannews.com:8003/user/queryByIds?userIds=" + JsonUtils.objectToJson(idSet);
		//ResponseEntity<GraceJSONResult> responseEntity
		//		= restTemplate.getForEntity(userServerUrlExecute, GraceJSONResult.class);
		//GraceJSONResult bodyResult = responseEntity.getBody();
		List<AppUserVO> publisherList = null;
		if (bodyResult.getStatus() == 200) {
			String userJson = JsonUtils.objectToJson(bodyResult.getData());
			publisherList = JsonUtils.jsonToList(userJson, AppUserVO.class);
		} else {
			publisherList = new ArrayList<>();
		}
		return publisherList;
	}


}
