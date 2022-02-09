package com.dongruan.api.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	// 定义交换机的名称
	public static final String EXCHANGE_ARTICLE = "exchange_article";

	// 定义队列的名称
	public static final String QUEUE_DOWNLOAD_HTML = "queue_download_html";

	// 创建交换机，放入springboot容器
	@Bean(EXCHANGE_ARTICLE)
	public Exchange exchange() {
		return ExchangeBuilder                      // 构建交换机
				.topicExchange(EXCHANGE_ARTICLE)    // 使用topic类型，并定义交换机的名称。https://www.rabbitmq.com/getstarted.html
				.durable(true)                      // 设置持久化，重启MQ后依然存在
				.build();
	}

	// 创建队列
	@Bean(QUEUE_DOWNLOAD_HTML)
	public Queue queue() {
		return new Queue(QUEUE_DOWNLOAD_HTML);
	}

	// 队列绑定交换机
	@Bean
	public Binding binding(
			@Qualifier(QUEUE_DOWNLOAD_HTML) Queue queue,
			@Qualifier(EXCHANGE_ARTICLE) Exchange exchange) {
		return BindingBuilder               // 定义绑定关系
				.bind(queue)                // 绑定队列
				.to(exchange)               // 到交换机
				.with("article.#.do")   // 定义路由规则（requestMapping映射）
				.noargs();                  // 执行绑定
	}
}

