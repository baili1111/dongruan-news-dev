package com.dongruan.api.interceptors;

import com.dongruan.constant.SystemConstant;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhu
 * @date 2022/1/19 22:01:27
 * @description
 */

public class AdminTokenInterceptor extends BaseInterceptor implements HandlerInterceptor {



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

		String adminUserId = request.getHeader("adminUserId");
		String adminUserToken = request.getHeader("adminUserToken");

		System.out.println("=====================================================================");
		System.out.println("AdminTokenInterceptor - adminUserId = " + adminUserId);
		System.out.println("AdminTokenInterceptor - adminUserToken = " + adminUserToken);
		System.out.println("=====================================================================");

		return verifyUserIdToken(adminUserId, adminUserToken, SystemConstant.REDIS_ADMIN_TOKEN);
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
