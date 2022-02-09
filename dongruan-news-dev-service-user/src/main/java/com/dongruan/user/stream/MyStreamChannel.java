package com.dongruan.user.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

/**
 * @author zhu
 * @date 2022/2/9 01:47:32
 * @description 构建通道channel
 * Sink
 * Source
 */
@Component
public interface MyStreamChannel {

	String INPUT = "myInput";
	String OUTPUT = "myOutput";

	@Input(MyStreamChannel.INPUT)
	SubscribableChannel input();

	@Output(MyStreamChannel.OUTPUT)
	MessageChannel output();

}

