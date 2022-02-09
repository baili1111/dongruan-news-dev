package com.dongruan.article.stream;

import com.dongruan.pojo.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author zhu
 * @date 2022/2/9 01:54:08
 * @description 开启绑定器
 *              绑定通道channel
 */
@Component
@EnableBinding(MyStreamChannel.class)
public class StreamServiceImpl implements StreamService {

	// 注入管道output，用于发送消息
	@Autowired
	private MyStreamChannel myStreamChannel;

	@Override
	public void sendStream() {
		AppUser user = new AppUser();
		user.setId("test1001");
		user.setNickname("dongruan");

		// 发送消息
		myStreamChannel.output().send(MessageBuilder.withPayload(user).build());
	}

	@Override
	public void eat(String dumpling) {
		myStreamChannel.output().send(MessageBuilder.withPayload(dumpling).build());
	}
}
