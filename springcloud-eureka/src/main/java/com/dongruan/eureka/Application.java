package com.dongruan.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author zhu
 * @date 2022/1/16 17:42:19
 * @description
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
		MongoAutoConfiguration.class,
		RabbitAutoConfiguration.class})
@EnableEurekaServer
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
