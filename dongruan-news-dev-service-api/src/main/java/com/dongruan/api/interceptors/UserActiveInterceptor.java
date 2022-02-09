package com.dongruan.api.interceptors;

import com.dongruan.constant.SystemConstant;
import com.dongruan.enums.UserStatus;
import com.dongruan.exception.GraceException;
import com.dongruan.grace.result.ResponseStatusEnum;
import com.dongruan.pojo.AppUser;
import com.dongruan.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author zhu
 * @date 2022/1/19 22:01:27
 * @description 用户激活状态检查拦截器
 * 发文章，修改文章等
 * 发评论，查看评论等
 * 查看我的粉丝等，这些媒体中心的功能必须用户激活后，才能进行，
 * 否则提示用户前往[账号设置]去修改信息
 */
public class UserActiveInterceptor extends BaseInterceptor implements HandlerInterceptor {


	/**
	 * 拦截请求，访问Controller之前
	 *
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String userId = request.getHeader("headerUserId");

		String userJson = redis.get(SystemConstant.REDIS_USER_INFO + ":" + userId);
		AppUser user = null;
		if (StringUtils.isNotBlank(userJson)) {
			user = JsonUtils.jsonToPojo(userJson, AppUser.class);
		} else {
			GraceException.display(ResponseStatusEnum.UN_LOGIN);
			return false;
		}

		// 如果不是激活状态则不能执行后续操作
		if (user.getActiveStatus() == null || user.getActiveStatus() != UserStatus.ACTIVE.type) {
			GraceException.display(ResponseStatusEnum.USER_INACTIVE_ERROR);
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
	 *
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
	 *
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
