package com.delicloud.app.miniprint.server.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.delicloud.app.miniprint.server.util.Constants;
import com.delicloud.app.miniprint.server.util.TokenUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RequestIegalInterceptor implements HandlerInterceptor {
	@Autowired
	private TokenUtil TokenUtil;
	

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
		//从header中得到token
		String header = request.getHeader(Constants.PRE_TOKEN);
		String[] signAndTime = TokenUtil.getSign(header);
		if (null == signAndTime || signAndTime.length != 2) {
			log.info(request.getRequestURI()+"参数不合法");
			return false;
		}
		return TokenUtil.checkSign(signAndTime, request.getRequestURI());
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}
	




}
