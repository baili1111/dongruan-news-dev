package com.dongruan.config.controller;

import com.dongruan.grace.result.GraceJSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhu
 * @date 2022/1/16 17:12:50
 * @description
 */
@RestController
public class HelloController {



	@GetMapping("/hello")
	public Object hello() {

		return GraceJSONResult.ok();
	};


}
