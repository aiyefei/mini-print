package com.delicloud.app.miniprint.core.dto;

import lombok.Data;

/**
 * @Author: dy
 * @Description: 设置个人信息入参
 * @Date: 2018/9/4 16:24
 */
@Data
public class SetUpPersonalInfoDto {

    /**
     * 头像 url
     */
    private String avatarUrl;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 所在区域
     */
    private String area;

    /**
     * 个性签名
     */
    private String individualitySignature;

}
