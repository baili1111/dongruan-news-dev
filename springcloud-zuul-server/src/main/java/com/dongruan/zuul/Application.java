package com.dongruan.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author zhu
 * @date 2022/1/16 17:42:19
 * @description
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
		MongoAutoConfiguration.class,
		RabbitAutoConfiguration.class})
@ComponentScan(basePackages = {"com.dongruan", "org.n3r.idworker"})
@EnableZuulProxy       // @EnableZuulProxy可以说是@EnableZuulServer的增强版，当Zuul与Eureka、Ribbon等组件配合使用时，则使用@EnableZuulProxy
//@EnableZuulServer
@EnableEurekaClient
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
