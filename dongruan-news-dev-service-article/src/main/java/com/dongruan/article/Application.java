package com.dongruan.article;

import com.rule.MyRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author zhu
 * @date 2022/1/16 17:42:19
 * @description
 */
@SpringBootApplication
@MapperScan(basePackages = "com.dongruan.article.mapper")
@ComponentScan(basePackages = {"com.dongruan", "org.n3r.idworker"})
@EnableEurekaClient
//@RibbonClient(name = "SERVICE-USER", configuration = MyRule.class)
@EnableFeignClients({"com.dongruan"})
@EnableHystrix
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
