package com.dongruan.api.interceptors;

import com.dongruan.exception.GraceException;
import com.dongruan.grace.result.ResponseStatusEnum;
import com.dongruan.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhu
 * @date 2022/1/21 23:07:44
 * @description
 */
public class BaseInterceptor {

	@Autowired
	public RedisOperator redis;

	public boolean verifyUserIdToken(String id, String token, String redisKeyPrefix) {

		if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(token)) {
			String redisToken = redis.get(redisKeyPrefix + ":" + id);
			if (StringUtils.isBlank(redisToken)) {
				GraceException.display(ResponseStatusEnum.UN_LOGIN);
				return false;
			} else {
				if (!redisToken.equalsIgnoreCase(token)) {
					GraceException.display(ResponseStatusEnum.TICKET_INVALID);
					return false;
				}
			}
		} else {
			GraceException.display(ResponseStatusEnum.UN_LOGIN);
			return false;
		}

		return true;
	}

}
