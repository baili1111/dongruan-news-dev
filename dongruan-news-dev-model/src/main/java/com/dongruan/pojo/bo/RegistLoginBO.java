package com.dongruan.pojo.bo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhu
 * @date 2022/1/19 22:52:23
 * @description
 */
public class RegistLoginBO {

	@NotBlank(message = "手机号不能为空")
	private String mobile;

	@NotBlank(message = "短信验证码不能为空")
	private String smsCode;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	@Override
	public String toString() {
		return "RegistLoginBO{" +
				"mobile='" + mobile + '\'' +
				", smsCode='" + smsCode + '\'' +
				'}';
	}
}
