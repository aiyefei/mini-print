package com.delicloud.app.miniprint.core.dto;

import lombok.Data;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/9/19 11:57
 */
@Data
public class PassRecallDto {

    private String mobile;
    private String password;
    private String region;
    private String verificationCode;

}
