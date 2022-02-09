package com.dongruan.article.stream;

import com.dongruan.pojo.AppUser;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * @author zhu
 * @date 2022/2/9 01:59:27
 * @description 构建消费端
 */
@Component
@EnableBinding(MyStreamChannel.class)
public class MyStreamConsumer {

	///**
	// * 监听并且实现消息的消费和相关业务处理
	// * @param user
	// */
	//@StreamListener(MyStreamChannel.INPUT)
	//public void receive(AppUser user) {
	//	System.out.println(user);
	//}

	@StreamListener(MyStreamChannel.INPUT)
	public void receive(String dumpling) {
		System.out.println(dumpling);
	}

}
