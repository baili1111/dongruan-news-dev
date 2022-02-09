package com.dongruan.api.controller.user.fallbacks;

import com.dongruan.api.controller.user.UserControllerApi;
import com.dongruan.constant.SystemConstant;
import com.dongruan.grace.result.GraceJSONResult;
import com.dongruan.grace.result.ResponseStatusEnum;
import com.dongruan.pojo.bo.UpdateUserInfoBO;
import com.dongruan.pojo.vo.AppUserVO;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhu
 * @date 2022/2/8 16:06:22
 * @description
 */
@Component
public class UserControllerFactoryFallBack implements FallbackFactory<UserControllerApi> {
	@Override
	public UserControllerApi create(Throwable throwable) {
		return new UserControllerApi() {
			@Override
			public GraceJSONResult getUserInfo(String userId) {
				return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR_FEIGN);
			}

			@Override
			public GraceJSONResult getAccountInfo(String userId) {
				return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR_FEIGN);
			}

			@Override
			public GraceJSONResult updateUserInfo(@Valid UpdateUserInfoBO updateUserInfoBO) {
				return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR_FEIGN);
			}

			@Override
			public GraceJSONResult queryByIds(String userIds) {
				System.out.println("进入客户端（服务调用者）的降级方法");
				List<AppUserVO> publisherList = new ArrayList<>();
				return GraceJSONResult.ok(publisherList);
			}
		};
	}
}
