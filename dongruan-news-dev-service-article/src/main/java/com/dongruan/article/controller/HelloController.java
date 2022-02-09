package com.dongruan.article.controller;

import com.dongruan.api.config.RabbitMQConfig;
import com.dongruan.api.config.RabbitMQDelayConfig;
import com.dongruan.api.controller.user.HelloControllerApi;
import com.dongruan.article.stream.StreamService;
import com.dongruan.grace.result.GraceJSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author zhu
 * @date 2022/1/16 17:12:50
 * @description
 */
@RestController
@RequestMapping("producer")
public class HelloController implements HelloControllerApi {

	final static Logger logger = LoggerFactory.getLogger(HelloController.class);

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private StreamService streamService;

	@RequestMapping("/stream")
	public Object streamProducer() {
		streamService.sendStream();

		for (int i = 0; i < 10; i++) {
			streamService.eat("我吃了第" + (i + 1) + "只饺子");
		}

		return GraceJSONResult.ok();
	}


	@Override
	@GetMapping("/hello")
	public Object hello() {

		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_ARTICLE, "article.hello", "这是从生产者发送的消息~");

		return GraceJSONResult.ok();
	}

	@GetMapping("/delay")
	public Object delay() {

		MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				// 设置消息持久
				message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
				// 设置延迟的时间，单位毫秒
				message.getMessageProperties().setDelay(5000);
				return message;
			}
		};

		rabbitTemplate.convertAndSend(
				RabbitMQDelayConfig.EXCHANGE_DELAY,
				"delay.demo",
				"这是一条延迟消息",
				messagePostProcessor
		);

		System.out.println("生产者发送延迟消息：" + new Date());

		return "OK";
	}



}
