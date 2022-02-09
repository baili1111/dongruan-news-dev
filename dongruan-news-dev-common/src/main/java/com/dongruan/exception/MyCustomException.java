package com.dongruan.exception;

import com.dongruan.grace.result.ResponseStatusEnum;

/**
 * @author zhu
 * @date 2022/1/19 22:16:58
 * @description 自定义异常
 *              目的：统一处理异常信息
 *                  便于解耦，service 与 controller 错误的解耦，不会被 service 返回的类型而限制
 */
public class MyCustomException extends RuntimeException {

	private ResponseStatusEnum responseStatusEnum;

	public MyCustomException(ResponseStatusEnum responseStatusEnum) {
		super("异常状态码为：" + responseStatusEnum.status()
				+ "；具体异常信息为" + responseStatusEnum.msg());
		this.responseStatusEnum = responseStatusEnum;
	}

	public ResponseStatusEnum getResponseStatusEnum() {
		return responseStatusEnum;
	}

	public void setResponseStatusEnum(ResponseStatusEnum responseStatusEnum) {
		this.responseStatusEnum = responseStatusEnum;
	}
}
