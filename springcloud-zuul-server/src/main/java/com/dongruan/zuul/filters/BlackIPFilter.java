package com.dongruan.zuul.filters;

import com.dongruan.grace.result.GraceJSONResult;
import com.dongruan.grace.result.ResponseStatusEnum;
import com.dongruan.utils.IPUtil;
import com.dongruan.utils.JsonUtils;
import com.dongruan.utils.RedisOperator;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhu
 * @date 2022/2/8 18:24:04
 * @description http://localhost:7080/actuator/refresh
 *  http://localhost:7080/actuator/bus-refresh
 *  精确通知：可以通过配置具体的客户端地址，来达道精确通知的目的：
 * http://localhost:7080/actuator/bus-refresh/{服务实例id}:{端口号}
 */
@Component
@RefreshScope
public class BlackIPFilter extends ZuulFilter {

	@Value("${blackIP.continueCounts}")
	private Integer continueCounts;
	@Value("${blackIP.timeInterval}")
	private Integer timeInterval;
	@Value("${blackIP.limitTimes}")
	private Integer limitTimes;

	@Autowired
	private RedisOperator redis;

	/**
	 * 定义过滤器类型
	 *      pre：    在请求被路由之前执行
	 *      route：  在路由请求的时候执行
	 *      post：   请求被路由以后执行
	 *      error：  处理请求时发生错误执行
	 * @return
	 */
	@Override
	public String filterType() {
		return "pre";
	}

	/**
	 * 过滤器的执行顺序，可以配置多个过滤器
	 * 执行顺序从小到大
	 * @return
	 */
	@Override
	public int filterOrder() {
		return 2;
	}

	/**
	 * 是否开启过滤器
	 *      true：使用
	 *      false：禁用
	 * @return
	 */
	@Override
	public boolean shouldFilter() {
		return true;
	}

	/**
	 * 过滤器业务实现
	 * @return
	 * @throws ZuulException
	 */
	@Override
	public Object run() throws ZuulException {
		System.out.println("执行【ip黑名单】过滤器。。。");

		System.out.println("continueCounts: " + continueCounts);
		System.out.println("timeInterval: " + timeInterval);
		System.out.println("limitTimes: " + limitTimes);

		// 获得上下文对象
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletRequest request = context.getRequest();

		// 获得ip
		String ip = IPUtil.getRequestIp(request);

		/**
		 * 需求：
		 * 判断ip在10秒内请求的次数是否超过10次，
		 * 如果超过，则限制访问15秒，15秒过后再放行
		 */

		final String ipRedisKey = "zuul-ip:" + ip;
		final String ipRedisLimitKey = "zuul-ip-limit:" + ip;

		// 获得当前ip这个key的剩余时间
		long limitLeftTime = redis.ttl(ipRedisLimitKey);
		// 如果剩余时间还存在，说明这个ip不能访问，继续等待
		if (limitLeftTime > 0) {
			stopRequest(context);
			return null;
		}

		// 在redis中累加ip的请求访问次数
		long requestCounts = redis.increment(ipRedisKey, 1);

		// 从0开始计算请求次数，初期访问为1，则设置过期时间，也就是连续请求的间隔时间
		if (requestCounts == 1) {
			redis.expire(ipRedisKey, timeInterval);
		}

		// 如果还能取得请求次数，说明用户连续请求的次数落在10秒内
		// 一旦请求次数超过了连续访问的次数，则需要限制这个ip了
		if (requestCounts > continueCounts) {
			// 限制ip访问一段时间
			redis.set(ipRedisLimitKey, ipRedisLimitKey, limitTimes);

			stopRequest(context);
		}

		return null; //没有意义可以不用管
	}

	private void stopRequest(RequestContext context){
		// 停止继续向下路由，禁止请求通信
		context.setSendZuulResponse(false);
		context.setResponseStatusCode(200);
		String result = JsonUtils.objectToJson(
				GraceJSONResult.errorCustom(
						ResponseStatusEnum.SYSTEM_ERROR_ZUUL));
		context.setResponseBody(result);
		context.getResponse().setCharacterEncoding("utf-8");
		context.getResponse().setContentType(MediaType.APPLICATION_JSON_VALUE);
	}

}
