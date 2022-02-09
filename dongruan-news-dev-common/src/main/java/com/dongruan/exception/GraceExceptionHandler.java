package com.dongruan.exception;

import com.dongruan.grace.result.GraceJSONResult;
import com.dongruan.grace.result.ResponseStatusEnum;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhu
 * @date 2022/1/19 22:21:24
 * @description 统一异常拦截处理
 *              可以针对异常的类型进行捕获，然后返回 json 信息到前端
 */
@ControllerAdvice
public class GraceExceptionHandler {

	/**
	 * 自定义异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MyCustomException.class)
	@ResponseBody
	public GraceJSONResult returnMyException(MyCustomException e) {
		e.printStackTrace();
		return GraceJSONResult.exception(e.getResponseStatusEnum());
	}

	/**
	 * 文件上传大小异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseBody
	public GraceJSONResult returnMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
		e.printStackTrace();
		return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_MAX_SIZE_ERROR);
	}


	/**
	 * 全局 BO 参数校验异常
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public GraceJSONResult handle(MethodArgumentNotValidException exception) {
		BindingResult result = exception.getBindingResult();
		Map<String, String> map = getErrors(result);
		return GraceJSONResult.errorMap(map);
	}

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


}
