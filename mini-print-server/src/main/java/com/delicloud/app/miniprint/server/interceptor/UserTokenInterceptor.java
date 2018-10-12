package com.delicloud.app.miniprint.server.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.delicloud.app.miniprint.core.entity.TToken;
import com.delicloud.app.miniprint.server.config.MvcConfig;
import com.delicloud.app.miniprint.server.util.Constants;
import com.delicloud.app.miniprint.server.util.TokenUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserTokenInterceptor implements HandlerInterceptor{

	@Autowired
	private TokenUtil TokenUtil;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {

		//从header中得到token
        String token = request.getHeader(Constants.PRE_TOKEN);
		TToken tokenModle = TokenUtil.getToken(token);
		if(TokenUtil.checkTokenPlat(tokenModle)) {
			request.setAttribute(MvcConfig.SESSION_USER, Long.parseLong(tokenModle.getUId()));
			return true;
		}else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}


}