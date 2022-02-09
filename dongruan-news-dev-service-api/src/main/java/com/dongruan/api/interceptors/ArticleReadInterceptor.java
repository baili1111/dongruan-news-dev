package com.dongruan.api.interceptors;

import com.dongruan.constant.SystemConstant;
import com.dongruan.utils.IPUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhu
 * @date 2022/1/19 22:01:27
 * @description
 */

public class ArticleReadInterceptor extends BaseInterceptor implements HandlerInterceptor {

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

		String articleId = request.getParameter("articleId");

		String userIP = IPUtil.getRequestIp(request);
		// 设置永久存在key，表示该ip已经阅读过了，无法累加阅读量
		boolean isExist = redis.keyIsExist(SystemConstant.REDIS_ARTICLE_ALREADY_READ + ":" + articleId + ":" + userIP);

		if (isExist) {
			return  false;
		}

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
