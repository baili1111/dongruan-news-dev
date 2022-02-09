package com.dongruan.utils.extend;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author zhu
 * @date 2022/1/24 02:01:23
 * @description
 */
@Component
@PropertySource("classpath:aliyun.properties")
@ConfigurationProperties(prefix = "aliyun")
public class AliyunResource {

	private String accessKeyID;

	private String accessKeySecret;

	public String getAccessKeyID() {
		return accessKeyID;
	}

	public void setAccessKeyID(String accessKeyID) {
		this.accessKeyID = accessKeyID;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}
}
