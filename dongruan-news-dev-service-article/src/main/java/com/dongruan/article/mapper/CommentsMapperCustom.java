package com.dongruan.article.mapper;

import com.dongruan.pojo.vo.CommentsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommentsMapperCustom {


	/**
	 * 查询文章的评论
	 */
	List<CommentsVO> queryArticleCommentList(@Param("paramsMap") Map<String, Object> map);


}