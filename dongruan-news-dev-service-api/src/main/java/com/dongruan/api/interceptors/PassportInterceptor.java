package com.dongruan.api.interceptors;

import com.dongruan.constant.SystemConstant;
import com.dongruan.exception.GraceException;
import com.dongruan.grace.result.ResponseStatusEnum;
import com.dongruan.utils.IPUtil;
import com.dongruan.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhu
 * @date 2022/1/19 22:01:27
 * @description
 */

public class PassportInterceptor implements HandlerInterceptor {

	@Autowired
	public RedisOperator redis;


	/**
	 * 拦截请求，访问Controller之前
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		// 获得用户ip
		String userIp = IPUtil.getRequestIp(request);

		boolean keyIsExist = redis.keyIsExist(SystemConstant.MOBILE_SMSCODE + ":" + userIp);
		if (keyIsExist) {
			GraceException.display(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
			//System.out.println("短信发送频率太大！");
			return false;
		}

		/**
		 * false: 请求被拦截
		 * true: 请求通过验证，放行
		 */
		return true;
	}

	/**
	 * 请求访问到controller之后，渲染视图之前
	 * @param request
	 * @param response
	 * @param handler
	 * @param modelAndView
	 * @throws Exception
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 请求访问到controller之后，渲染视图之后
	 * @param request
	 * @param response
	 * @param handler
	 * @param ex
	 * @throws Exception
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}
}
