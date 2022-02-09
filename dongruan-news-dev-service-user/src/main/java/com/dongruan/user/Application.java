package com.dongruan.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author zhu
 * @date 2022/1/16 17:42:19
 * @description
 */
@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@MapperScan(basePackages = "com.dongruan.user.mapper")
@ComponentScan(basePackages = {"com.dongruan", "org.n3r.idworker"})
@EnableEurekaClient
@EnableCircuitBreaker
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
