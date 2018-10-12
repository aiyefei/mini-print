package com.delicloud.app.miniprint.server.config;

import com.delicloud.app.miniprint.server.interceptor.RequestIegalInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.delicloud.app.miniprint.server.interceptor.UserTokenInterceptor;
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
	
	public final static String SESSION_USER = "sessionUser";
	@Autowired
	private UserTokenInterceptor userTokenInterceptor;

	@Autowired
	private RequestIegalInterceptor requestIegalInterceptor;


	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		InterceptorRegistration requestIegalRegistration = registry.addInterceptor(requestIegalInterceptor);
		requestIegalRegistration.excludePathPatterns("/app/getVersion");

		requestIegalRegistration.addPathPatterns("/**");


		// ======================================================= //

		InterceptorRegistration userTokenRegistration = registry.addInterceptor(userTokenInterceptor);

		userTokenRegistration.excludePathPatterns("/user/login");
		userTokenRegistration.excludePathPatterns("/user/register");
		userTokenRegistration.excludePathPatterns("/user/getVerificationCode");
		userTokenRegistration.excludePathPatterns("/user/passRecall");
		userTokenRegistration.excludePathPatterns("/user/logout");
		userTokenRegistration.excludePathPatterns("/app/getVersion");

		userTokenRegistration.addPathPatterns("/**");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
		super.addResourceHandlers(registry);
	}

}
