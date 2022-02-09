package com.dongruan.article;

import com.dongruan.api.config.RabbitMQConfig;
import com.dongruan.api.config.RabbitMQDelayConfig;
import com.dongruan.article.service.ArticleService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author zhu
 * @date 2022/2/7 00:59:15
 * @description
 */
@Component
public class RabbitMQDelayConsumer {

	@Autowired
	private ArticleService articleService;

	/**
	 *	监听消息队列
     * @param payload 消息内容
     * @param message 消息对象
     */
	@RabbitListener(queues = {RabbitMQDelayConfig.QUEUE_DELAY})
	public void watchQueue(String payload, Message message) {
		System.out.println(payload);

		String routingKey = message.getMessageProperties().getReceivedRoutingKey();
		System.out.println(routingKey);

		System.out.println("消费者接受延迟消息：" + new Date());

		// 消费者接受到定时发送的延迟消息，修改当前的文章状态为‘即时发布’
		String articleId = payload;
		articleService.updateArticleAppointToPublish(articleId);
	}

}
