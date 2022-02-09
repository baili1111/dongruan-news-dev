package com.dongruan.files.controller;

import com.dongruan.api.controller.user.HelloControllerApi;
import com.dongruan.grace.result.GraceJSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhu
 * @date 2022/1/16 17:12:50
 * @description
 */
@RestController
public class HelloController implements HelloControllerApi {

	final static Logger logger = LoggerFactory.getLogger(HelloController.class);

	public Object hello() {
		return GraceJSONResult.ok("Hello World");
	}

}
