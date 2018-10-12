package com.delicloud.app.miniprint.core.dto;

import lombok.Data;

import com.delicloud.platform.common.lang.util.JsonUtil;

/**
 * @Author: dy
 * @Description: 用户注册入参
 * @Date: 2018/9/4 15:40
 */
@Data
public class UserRegistrationDto {

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 可选，手机号区域，默认86
     */
    private String region;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 验证码
     */
    private String verificationCode;

    /**
     * 设备标识
     */
    private String fId;
    
    public static void main(String[] args) {
    	UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    	System.out.println(JsonUtil.getJsonFromObject(userRegistrationDto)); 
	}

}
