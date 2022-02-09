package com.dongruan.api.controller;


import com.dongruan.constant.SystemConstant;
import com.dongruan.grace.result.GraceJSONResult;
import com.dongruan.pojo.vo.AppUserVO;
import com.dongruan.utils.JsonUtils;
import com.dongruan.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseController {

	@Autowired
	public RedisOperator redis;

	@Autowired
	public RestTemplate restTemplate;

	@Value("${website.domain-name}")
	private String DOMAIN_NAME;


	/**
	 * 获取BO中的错误信息
	 *
	 * @param result
	 * @return
	 */
	public Map<String, String> getErrors(BindingResult result) {
		Map<String, String> map = new HashMap<>();
		List<FieldError> errorList = result.getFieldErrors();
		for (FieldError error : errorList) {
			// 发送验证错误的时候所对应的某个属性
			String field = error.getField();

			// 验证的错误消息
			String msg = error.getDefaultMessage();
			map.put(field, msg);
		}
		return map;
	}

	public void setCookie(HttpServletRequest request,
	                      HttpServletResponse response,
	                      String cookieName,
	                      String cookieValue,
	                      Integer maxAge) {

		try {
			cookieValue = URLEncoder.encode(cookieValue, "utf-8");
			setCookieValue(request, response, cookieName, cookieValue, maxAge);
			//Cookie cookie = new Cookie(cookieName, cookieValue);
			//cookie.setMaxAge(maxAge);
			//cookie.setDomain("dongruannews.com");
			//cookie.setPath("/");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	public void setCookieValue(HttpServletRequest request,
	                           HttpServletResponse response,
	                           String cookieName,
	                           String cookieValue,
	                           Integer maxAge) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(maxAge);
		//cookie.setDomain("dongruannews.com");
		cookie.setDomain(DOMAIN_NAME);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	public void deleteCookie(HttpServletRequest request,
	                         HttpServletResponse response,
	                         String cookieName) {
		try {
			String deleteValue = URLEncoder.encode("", "utf-8");
			setCookieValue(request, response, cookieName, deleteValue, SystemConstant.COOKIE_DELETE);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public Integer getCountsFromRedis(String key) {
		String countsStr = redis.get(key);
		if (StringUtils.isBlank(countsStr)) {
			countsStr = "0";
		}
		return Integer.valueOf(countsStr);
	}

	// 发起远程调用，获得用户的基本信息
	public List<AppUserVO> getBasicUserList(Set idSet) {
		String userServerUrlExecute =
				"http://user.dongruannews.com:8003/user/queryByIds?userIds=" + JsonUtils.objectToJson(idSet);
		ResponseEntity<GraceJSONResult> responseEntity
				= restTemplate.getForEntity(userServerUrlExecute, GraceJSONResult.class);
		GraceJSONResult bodyResult = responseEntity.getBody();
		List<AppUserVO> userVOList = null;
		if (bodyResult.getStatus() == 200) {
			String userJson = JsonUtils.objectToJson(bodyResult.getData());
			userVOList = JsonUtils.jsonToList(userJson, AppUserVO.class);
		}
		return userVOList;
	}

}
