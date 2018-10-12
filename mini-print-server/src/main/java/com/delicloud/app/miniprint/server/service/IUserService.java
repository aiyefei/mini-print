package com.delicloud.app.miniprint.server.service;

import com.delicloud.app.miniprint.core.bo.UserBo;
import com.delicloud.app.miniprint.core.dto.*;
import com.delicloud.app.miniprint.server.dto.LoginDto;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/8/31 16:16
 */
public interface IUserService {

    /**
     * 获取验证码
     * @param dto
     */
    void getVerifyCode(UserDto dto);

    /**
     * 用户注册
     * @param userRegistrationDto 用户注册入参
     */
    LoginDto register(UserRegistrationDto userRegistrationDto);

    /**
     * 用户登录
     * @param dto 入参
     */
    LoginDto login(UserDto dto);

    /**
     * 修改用户密码
     * @param dto 入参
     */
    void updatePwd(Long uid, UpdatePwdDto dto);

    /**
     * 找回密码
     * @param dto 入参
     */
    void passRecall(PassRecallDto dto);

    /**
     * 设置个人信息
     * @param setUpPersonalInfoDto
     */
    void setUpPersonalInfo(Long uid, SetUpPersonalInfoDto setUpPersonalInfoDto);

    /**
     * 获取用户所有信息
     * @param dto 入参
     */
    UserBo getUserInfo(Long uid, UserInfoDto dto);

    /**
     * 用户登出
     * @param uid 用户 id
     */
    void logout(Long uid);
}
