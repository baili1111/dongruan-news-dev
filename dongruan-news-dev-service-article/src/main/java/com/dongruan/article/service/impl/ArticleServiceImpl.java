package com.dongruan.article.service.impl;

import com.dongruan.api.config.RabbitMQConfig;
import com.dongruan.api.config.RabbitMQDelayConfig;
import com.dongruan.api.service.BaseService;
import com.dongruan.article.mapper.ArticleMapper;
import com.dongruan.article.mapper.ArticleMapperCustom;
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
import com.dongruan.utils.DateUtil;
import com.dongruan.utils.PagedGridResult;
import com.dongruan.utils.extend.AliTextReviewUtils;
import com.github.pagehelper.PageHelper;
import com.mongodb.client.gridfs.GridFSBucket;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.n3r.idworker.Sid;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author zhu
 * @date 2022/1/30 23:57:53
 * @description
 */
@Service
public class ArticleServiceImpl extends BaseService implements ArticleService {


	@Autowired
	private ArticleMapper articleMapper;

	@Autowired
	private ArticleMapperCustom articleMapperCustom;

	@Autowired
	private Sid sid;

	@Autowired
	private AliTextReviewUtils aliTextReviewUtils;


	@Override
	public void createArticle(NewArticleBO newArticleBO, Category category) {

		String articleId = sid.nextShort();

		Article article = new Article();
		BeanUtils.copyProperties(newArticleBO, article);

		article.setId(articleId);

		article.setCategoryId(category.getId());
		article.setArticleStatus(ArticleReviewStatus.REVIEWING.type);
		article.setCommentCounts(0);
		article.setReadCounts(0);

		article.setIsDelete(YesOrNo.NO.type);
		article.setCreateTime(new Date());
		article.setUpdateTime(new Date());

		// ?????????????????????????????????????????????????????????????????????????????????????????????
		if (article.getIsAppoint() == ArticleAppointType.TIMING.type) {
			article.setPublishTime(newArticleBO.getPublishTime());
		} else if (article.getIsAppoint() == ArticleAppointType.IMMEDIATELY.type) {
			article.setPublishTime(new Date());
		}

		int res = articleMapper.insert(article);
		if (res != 1) {
			GraceException.display(ResponseStatusEnum.ARTICLE_CREATE_ERROR);
		}

		// ?????????????????????mq????????????????????????????????????????????????????????????????????????????????????
		if (article.getIsAppoint() == ArticleAppointType.TIMING.type) {
			Date endDate = newArticleBO.getPublishTime();
			Date startDate = new Date();

            //int delayTimes = (int)(endDate.getTime() - startDate.getTime());

			System.out.println(DateUtil.timeBetween(startDate, endDate));

			// ?????????????????????????????????10s
			int delayTimes = 60 * 1000;

			// ??????????????????????????????id
			String cid = UUID.randomUUID().toString();
			CorrelationData correlationData = new CorrelationData(cid);

			MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
				@Override
				public Message postProcessMessage(Message message) throws AmqpException {
					// ????????????
					message.getMessageProperties()
							.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
					// ????????????????????????????????????
					message.getMessageProperties().setDelay(delayTimes);
					return message;
				}
			};

			// ??????????????????
			rabbitTemplate.convertAndSend(
					RabbitMQDelayConfig.EXCHANGE_DELAY,
					"publish.delay.display",
					articleId,
					messagePostProcessor);

			System.out.println("????????????-???????????????" + new Date());
		}



		// ??????????????????AI??????????????????????????????????????????????????????
		//String reviewTextResult = aliTextReviewUtils.reviewTextContent(newArticleBO.getContent());
		String reviewTextResult = ArticleReviewLevel.REVIEW.type;

		if (reviewTextResult.equalsIgnoreCase(ArticleReviewLevel.PASS.type)) {
			// ???????????????????????? ???????????????????????????
			this.updateArticleStatus(articleId, ArticleReviewStatus.SUCCESS.type);

		} else if (reviewTextResult.equalsIgnoreCase(ArticleReviewLevel.REVIEW.type)) {
			// ?????????????????????????????????????????????????????????
			this.updateArticleStatus(articleId, ArticleReviewStatus.WAITING_MANUAL.type);

		} else if (reviewTextResult.equalsIgnoreCase(ArticleReviewLevel.BLOCK.type)) {
			// ??????????????????????????????????????????????????????
			this.updateArticleStatus(articleId, ArticleReviewStatus.FAILED.type);
		}

	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateAppointToPublish() {
		articleMapperCustom.updateAppointToPublish();
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateArticleAppointToPublish(String articleId) {
		Article article = new Article();
		article.setId(articleId);
		article.setIsAppoint(ArticleAppointType.IMMEDIATELY.type);
		articleMapper.updateByPrimaryKeySelective(article);

	}

	@Override
	public PagedGridResult queryMyArticleList(String userId, String keyword, Integer status, Date startDate,
	                                          Date endDate, Integer page, Integer pageSize) {

		Example example = new Example(Article.class);
		example.orderBy("createTime").desc();
		Example.Criteria criteria = example.createCriteria();

		criteria.andEqualTo("publishUserId", userId);

		if (StringUtils.isNotBlank(keyword)) {
			criteria.andLike("title", "%" + keyword + "%");
		}

		if (ArticleReviewStatus.isArticleStatusValid(status)) {
			criteria.andEqualTo("articleStatus", status);
		}

		// ????????????????????????????????????????????????????????????????????????
		if (status != null && status == 12) {
			criteria.andEqualTo("articleStatus", ArticleReviewStatus.REVIEWING.type)
					.orEqualTo("articleStatus", ArticleReviewStatus.WAITING_MANUAL.type);
		}

		//isDelete ?????????0
		criteria.andEqualTo("isDelete", YesOrNo.NO.type);

		if (startDate != null) {
			criteria.andGreaterThanOrEqualTo("publishTime", startDate);
		}
		if (endDate != null) {
			criteria.andLessThanOrEqualTo("publishTime", endDate);
		}

		/**
		 * page: ?????????
		 * pageSize: ??????????????????
		 */
		PageHelper.startPage(page, pageSize);
		List<Article> list = articleMapper.selectByExample(example);
		return setterPageGrid(list, page);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateArticleStatus(String articleId, Integer pendingStatus) {
		Example example = new Example(Article.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", articleId);

		Article pendingArticle = new Article();
		pendingArticle.setArticleStatus(pendingStatus);

		int res = articleMapper.updateByExampleSelective(pendingArticle, example);
		if (res != 1) {
			GraceException.display(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
		}
	}

	@Override
	public PagedGridResult queryAllArticleListAdmin(Integer status, Integer page, Integer pageSize) {
		Example articleExample = new Example(Article.class);
		articleExample.orderBy("createTime").desc();

		Example.Criteria criteria = articleExample.createCriteria();
		if (ArticleReviewStatus.isArticleStatusValid(status)) {
			criteria.andEqualTo("articleStatus", status);
		}

		// ????????????????????????????????????????????????????????????????????????
		if (status != null && status == 12) {
			criteria.andEqualTo("articleStatus", ArticleReviewStatus.REVIEWING.type)
					.orEqualTo("articleStatus", ArticleReviewStatus.WAITING_MANUAL.type);
		}

		//isDelete ?????????0
		criteria.andEqualTo("isDelete", YesOrNo.NO.type);

		/**
		 * page: ?????????
		 * pageSize: ??????????????????
		 */
		PageHelper.startPage(page, pageSize);
		List<Article> list = articleMapper.selectByExample(articleExample);
		return setterPageGrid(list, page);
	}

	@Transactional
	@Override
	public void deleteArticle(String userId, String articleId) {
		Example articleExample = makeExampleCriteria(userId, articleId);

		Article pending = new Article();
		pending.setIsDelete(YesOrNo.YES.type);

		int result = articleMapper.updateByExampleSelective(pending, articleExample);
		if (result != 1) {
			GraceException.display(ResponseStatusEnum.ARTICLE_DELETE_ERROR);
		}

		// ??????????????????????????????????????????html
		deleteHTML(articleId);
	}

	@Autowired
	private GridFSBucket gridFSBucket;
	/**
	 * ??????????????????????????????????????????html
	 */
	private void deleteHTML(String articleId) {
		// 1. ???????????????mongoFileId
		Article pending = articleMapper.selectByPrimaryKey(articleId);
		String articleMongoId = pending.getMongoFileId();

		// 2. ??????GridFS????????????
		gridFSBucket.delete(new ObjectId(articleMongoId));

		// 3. ??????????????????HTML??????
        //doDeleteArticleHTML(articleId);
		doDeleteArticleHTMLByMQ(articleId);
	}

	@Autowired
	private RabbitTemplate rabbitTemplate;
	private void doDeleteArticleHTMLByMQ(String articleId) {
		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_ARTICLE,
				"article.delete.do", articleId);
	}

	@Autowired
	public RestTemplate restTemplate;
	private void doDeleteArticleHTML(String articleId) {
		String url = "http://html.dongruannews.com:8002/article/html/delete?articleId=" + articleId;
		ResponseEntity<Integer> responseEntity = restTemplate.getForEntity(url, Integer.class);
		int status = responseEntity.getBody();
		if (status != HttpStatus.OK.value()) {
			GraceException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
		}
	}

	@Transactional
	@Override
	public void withdrawArticle(String userId, String articleId) {
		Example articleExample = makeExampleCriteria(userId, articleId);

		Article pending = new Article();
		pending.setArticleStatus(ArticleReviewStatus.WITHDRAW.type);

		int result = articleMapper.updateByExampleSelective(pending, articleExample);
		if (result != 1) {
			GraceException.display(ResponseStatusEnum.ARTICLE_WITHDRAW_ERROR);
		}

		// ??????????????????????????????????????????html
		deleteHTML(articleId);
	}


	@Transactional
	@Override
	public void updateArticleToGridFS(String articleId, String articleMongoId) {
		Article pendingArticle = new Article();
		pendingArticle.setId(articleId);
		pendingArticle.setMongoFileId(articleMongoId);
		articleMapper.updateByPrimaryKeySelective(pendingArticle);
	}


	private Example makeExampleCriteria(String userId, String articleId) {
		Example articleExample = new Example(Article.class);
		Example.Criteria criteria = articleExample.createCriteria();
		criteria.andEqualTo("publishUserId", userId);
		criteria.andEqualTo("id", articleId);
		return articleExample;
	}
}
