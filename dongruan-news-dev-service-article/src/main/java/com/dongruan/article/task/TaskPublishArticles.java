package com.dongruan.article.task;

import com.dongruan.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * @author zhu
 * @date 2022/1/31 00:45:41
 * @description
 */
//@Configuration //1.标记配置类，使得springboot容器扫描到
//@EnableScheduling //2.开启定时任务
public class TaskPublishArticles {

	@Autowired
	private ArticleService articleService;

	//3. 添加定时任务
	@Scheduled(cron = "0/10 * * * * ?")
	private void publishArticle() {
		//System.out.println("执行定时任务: " + LocalDateTime.now());

		//4.调用文章service，把当前时间应该发布的定时文章，状态改为即时
		articleService.updateAppointToPublish();
	}
}
