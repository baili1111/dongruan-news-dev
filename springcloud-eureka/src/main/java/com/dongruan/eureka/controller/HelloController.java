package com.dongruan.eureka.controller;

import com.dongruan.grace.result.GraceJSONResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhu
 * @date 2022/2/7 21:36:47
 * @description
 */
@RestController
public class HelloController {

	@GetMapping("/hello")
	public GraceJSONResult hello() {
		return GraceJSONResult.ok();
	}

}
