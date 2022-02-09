package com.dongruan.utils;

import com.dongruan.utils.extend.TencentyunResource;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

//导入可选配置类
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;

// 导入对应SMS模块的client
import com.tencentcloudapi.sms.v20210111.SmsClient;

// 导入要请求接口对应的request response类
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhu
 * @date 2022/1/18 23:42:51
 * @description
 */
@Component
public class SMSUtils {

	@Autowired
	public TencentyunResource tencentyunResource;

	public void sendSMS(String mobile, String code) {
		try{
			// 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
			// 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
			Credential cred = new Credential(tencentyunResource.getSecretId(), tencentyunResource.getSecretKey());
			// 实例化一个http选项，可选的，没有特殊需求可以跳过
			HttpProfile httpProfile = new HttpProfile();
			httpProfile.setEndpoint("sms.tencentcloudapi.com");
			// 实例化一个client选项，可选的，没有特殊需求可以跳过
			ClientProfile clientProfile = new ClientProfile();
			clientProfile.setHttpProfile(httpProfile);
			// 实例化要请求产品的client对象,clientProfile是可选的
			SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);
			// 实例化一个请求对象,每个接口都会对应一个request对象
			SendSmsRequest req = new SendSmsRequest();
			String[] phoneNumberSet1 = {mobile};
			req.setPhoneNumberSet(phoneNumberSet1);

			req.setSmsSdkAppId(tencentyunResource.getSdkAppID());
			req.setSignName(tencentyunResource.getSignName());
			req.setTemplateId(tencentyunResource.getTemplateId());

			String[] templateParamSet1 = {code};
			req.setTemplateParamSet(templateParamSet1);

			// 返回的resp是一个SendSmsResponse的实例，与请求对象对应
			SendSmsResponse resp = client.SendSms(req);
			// 输出json格式的字符串回包
			System.out.println(SendSmsResponse.toJsonString(resp));
		} catch (TencentCloudSDKException e) {
			System.out.println(e.toString());
		}
	}

}
