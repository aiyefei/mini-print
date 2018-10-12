package com.delicloud.app.miniprint.server.interceptor;

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.delicloud.app.miniprint.server.util.DateUtil;
import com.delicloud.platform.common.lang.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Aspect
@Component
public class LogAspect {
	ThreadLocal<Long> startTime = new ThreadLocal<>();

	@Pointcut("execution(* com.delicloud.app.miniprint.server.controller.*.*(..))")
	public void webLog() {
	}

	@Before("webLog()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		startTime.set(System.currentTimeMillis());

		// 接收到请求，记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		 //记录下请求内容
		log.info("REQUEST IP : {} URI : {} , Params : {}", getIpAddr(request) , request.getRequestURI(), Arrays.toString(joinPoint.getArgs()));

	}

	@AfterReturning(returning = "ret", pointcut = "webLog()")
	public void doAfterReturning(Object ret) throws Throwable {
		// 处理完请求，返回内容
		log.info("RESPONSE : " + JsonUtil.getJsonFromObject(ret));
		log.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()) + "ms");
	}


	// 获取客户端IP
	private String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
