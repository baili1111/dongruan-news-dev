package com.dongruan.utils.extend;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author zhu
 * @date 2022/1/18 22:12:19
 * @description
 */
@Component
@PropertySource("classpath:tencentyun.properties")
@ConfigurationProperties(prefix = "tencentyun")
public class TencentyunResource {

	private String sdkAppID;

	private String appKey;

	private String secretId;

	private String SecretKey;

	private String signName;

	private String templateId;

	public String getSdkAppID() {
		return sdkAppID;
	}

	public void setSdkAppID(String sdkAppID) {
		this.sdkAppID = sdkAppID;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getSecretId() {
		return secretId;
	}

	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}

	public String getSecretKey() {
		return SecretKey;
	}

	public void setSecretKey(String secretKey) {
		SecretKey = secretKey;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
}
