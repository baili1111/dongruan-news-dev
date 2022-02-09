package com.rule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhu
 * @date 2022/2/8 13:31:40
 * @description 需要注意，自定义的规则要放在一个独立的包，要不然被目前的容器扫描到后，
 * 那么所有的ribbon负载均衡策略都会被这个自定义的影响
 */
@Configuration
public class MyRule {
	@Bean
	public IRule irule() {
		return new RandomRule();
	}
}

