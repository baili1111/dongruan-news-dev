package com.dongruan.zuul.controller;

import com.dongruan.api.controller.user.HelloControllerApi;
import com.dongruan.grace.result.GraceJSONResult;
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
public class HelloController {

	final static Logger logger = LoggerFactory.getLogger(HelloController.class);


	@GetMapping("/hello")
	public Object hello() {

		return GraceJSONResult.ok();
	};


}
