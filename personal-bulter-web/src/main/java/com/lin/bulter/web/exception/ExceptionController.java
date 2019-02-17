package com.lin.bulter.web.exception;

import com.alibaba.fastjson.JSONObject;
import com.lin.bulter.common.utils.WebResultUtil;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionController {

	// 捕捉shiro的异常
	@ExceptionHandler(ShiroException.class)
	public JSONObject handle401(ShiroException e) {
		return WebResultUtil.renderSuccess(401, e.getMessage().toString());
	}

	// 捕捉UnauthorizedException
	@ExceptionHandler(UnauthorizedException.class)
	public JSONObject handle401(UnauthorizedException e) {
		return WebResultUtil.renderSuccess(401, "Unauthorized");
	}

	// 捕捉其他所有异常
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public JSONObject globalException(HttpServletRequest request, Throwable ex) {
		return WebResultUtil.renderSuccess(getStatus(request).value(), ex.getMessage());
	}

	private HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return HttpStatus.valueOf(statusCode);
	}
}
