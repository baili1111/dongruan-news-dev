package com.dongruan.exception;

import com.dongruan.grace.result.ResponseStatusEnum;

/**
 * @author zhu
 * @date 2022/1/19 22:16:15
 * @description 处理异常，统一封装
 */
public class GraceException {

	/**
	 * 注册ip60秒内不允许获取多次验证码 异常
	 * @param responseStatusEnum
	 */
	public static void display(ResponseStatusEnum responseStatusEnum) {
		throw new MyCustomException(responseStatusEnum);
	}

}
