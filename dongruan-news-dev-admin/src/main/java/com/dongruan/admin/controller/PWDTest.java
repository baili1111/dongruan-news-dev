package com.dongruan.admin.controller;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * @author zhu
 * @date 2022/1/24 22:09:06
 * @description
 */
public class PWDTest {

	public static void main(String[] args) {
		String pwd = BCrypt.hashpw("admin", BCrypt.gensalt());
		System.out.println(pwd);
	}

}
