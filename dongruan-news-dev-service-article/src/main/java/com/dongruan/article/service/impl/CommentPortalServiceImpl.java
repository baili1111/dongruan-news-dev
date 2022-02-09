package com.dongruan.article.service.impl;

import com.dongruan.api.service.BaseService;
import com.dongruan.article.mapper.CommentsMapper;
import com.dongruan.article.mapper.CommentsMapperCustom;
import com.dongruan.article.service.ArticlePortalService;
import com.dongruan.article.service.CommentPortalService;
import com.dongruan.constant.SystemConstant;
import com.dongruan.pojo.Comments;
import com.dongruan.pojo.vo.ArticleDetailVO;
import com.dongruan.pojo.vo.CommentsVO;
import com.dongruan.utils.PagedGridResult;
import com.github.pagehelper.PageHelper;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhu
 * @date 2022/1/30 23:57:53
 * @description
 */
@Service
public class CommentPortalServiceImpl extends BaseService implements CommentPortalService {


	@Autowired
	private CommentsMapper commentsMapper;

	@Autowired
	private ArticlePortalService articlePortalService;

	@Autowired
	private Sid sid;

	@Autowired
	private CommentsMapperCustom commentsMapperCustom;


	@Transactional
	@Override
	public void createComment(String articleId, String fatherCommentId, String content,
	                          String userId, String nickname, String face) {


		ArticleDetailVO article = articlePortalService.queryDetail(articleId);

		Comments comments = new Comments();
		comments.setId(sid.nextShort());

		comments.setWriterId(article.getPublishUserId());
		comments.setArticleTitle(article.getTitle());
		comments.setArticleCover(article.getCover());
		comments.setArticleId(articleId);

		comments.setFatherId(fatherCommentId);
		comments.setCommentUserId(userId);
		comments.setCommentUserNickname(nickname);
		comments.setCommentUserFace(face);

		comments.setContent(content);
		comments.setCreateTime(new Date());

		commentsMapper.insert(comments);

		// 评论数累加
		redis.increment(SystemConstant.REDIS_ARTICLE_COMMENT_COUNTS + ":" + articleId, 1);

	}

	@Override
	public PagedGridResult queryArticleComments(String articleId, Integer page, Integer pageSize) {
		Map<String, Object> map = new HashMap<>();
		map.put("articleId", articleId);

		PageHelper.startPage(page, pageSize);
		List<CommentsVO> list = commentsMapperCustom.queryArticleCommentList(map);

		return setterPageGrid(list, page);
	}

	@Override
	public PagedGridResult queryWriterCommentsMng(String writerId, Integer page, Integer pageSize) {

		Comments comment = new Comments();
		comment.setWriterId(writerId);

		PageHelper.startPage(page, pageSize);
		List<Comments> list = commentsMapper.select(comment);
		return setterPageGrid(list, page);
	}

	@Override
	public void deleteComment(String writerId, String commentId) {
		Comments comment = new Comments();
		comment.setId(commentId);
		comment.setWriterId(writerId);

		commentsMapper.delete(comment);
	}

}
