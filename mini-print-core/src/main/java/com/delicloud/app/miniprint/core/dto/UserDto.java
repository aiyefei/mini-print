package com.delicloud.app.miniprint.core.dto;

import lombok.Data;

/**
 * @Author: dy
 * @Description: 用户信息dto
 * @Date: 2018/9/19 11:32
 */
@Data
public class UserDto {

    private Long uid;
    private String mobile;
    /**
     * 短信类型 1注册/2密码找回
     */
    private String type;
    private String region;
    private String password;
}
