package com.dongruan.user.controller;

import com.dongruan.api.controller.user.HelloControllerApi;
import com.dongruan.grace.result.DONGRUANJSONResult;
import com.dongruan.grace.result.GraceJSONResult;
import com.dongruan.grace.result.ResponseStatusEnum;
import com.dongruan.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhu
 * @date 2022/1/16 17:12:50
 * @description
 */
@RestController
public class HelloController implements HelloControllerApi {

	final static Logger logger = LoggerFactory.getLogger(HelloController.class);

	@Autowired
	private RedisOperator redis;

	@Override
	public Object hello() {


		logger.debug("debug: hello~");
		logger.info("info: hello~");
		logger.warn("warn: hello~");
		logger.error("error: hello~");


		//return "hello";
		//return DONGRUANJSONResult.ok();
		//return DONGRUANJSONResult.ok("hello");
		//return DONGRUANJSONResult.errorMsg("您的信息有误");
		return GraceJSONResult.ok();
	};

	@GetMapping("/redis")
	public Object redis() {
		redis.set("name", "风间影月");
		return GraceJSONResult.ok(redis.get("name"));
	}

}
