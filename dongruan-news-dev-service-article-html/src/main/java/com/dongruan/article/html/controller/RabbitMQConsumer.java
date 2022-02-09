package com.dongruan.article.html.controller;

import com.dongruan.api.config.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhu
 * @date 2022/2/7 00:59:15
 * @description
 */
@Component
public class RabbitMQConsumer {

	@Autowired
	private ArticleHTMLComponent articleHTMLComponent;

	/**
	 * 监听消息队列
	 * @param payload 消息内容
	 * @param message 消息对象
	 */
	@RabbitListener(queues = {RabbitMQConfig.QUEUE_DOWNLOAD_HTML})
	public void watchQueue(String payload, Message message) {
		System.out.println(payload);

		String routingKey = message.getMessageProperties().getReceivedRoutingKey();
		//if (routingKey.equalsIgnoreCase("article.publish.download.do")) {
		//	System.out.println("article.publish.download.do");
		//} else if (routingKey.equalsIgnoreCase("article.success.do")) {
		//	System.out.println("article.success.do");
		//} else {
		//	System.out.println("不符合的规则：" + routingKey);
		//}

		if(routingKey.equalsIgnoreCase("article.download.do")) {
			System.out.println("下载HTML");

			String articleId = payload.split(",")[0];
			String articleMongoId = payload.split(",")[1];

			try {
				articleHTMLComponent.download(articleId, articleMongoId);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (routingKey.equalsIgnoreCase("article.delete.do")) {

			String articleId = payload;
			try {
				articleHTMLComponent.delete(articleId);
			} catch (Exception e) {
				e.printStackTrace();
			}


		}


	}

}
