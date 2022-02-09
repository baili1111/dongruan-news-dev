package com.dongruan.article.mapper;

import com.dongruan.my.mapper.MyMapper;
import com.dongruan.pojo.Article;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleMapper extends MyMapper<Article> {
}