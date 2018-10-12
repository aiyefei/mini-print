package com.delicloud.app.miniprint.server.controller;

import com.delicloud.app.miniprint.core.bo.UserBo;
import com.delicloud.app.miniprint.core.dto.*;
import com.delicloud.app.miniprint.core.vo.ResponseVo;
import com.delicloud.app.miniprint.server.config.MvcConfig;
import com.delicloud.app.miniprint.server.dto.LoginDto;
import com.delicloud.app.miniprint.server.service.IFileService;
import com.delicloud.app.miniprint.server.service.IUserService;
import com.delicloud.platform.common.lang.bo.RespBase;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: dy
 * @Description: 用户前端控制器
 * @Date: 2018/8/30 16:11
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @ApiOperation("获取手机验证码")
    @PostMapping("/getVerificationCode")
    public RespBase getVerificationCode (@RequestBody UserDto dto) {

        userService.getVerifyCode(dto);

        return ResponseVo.OK_RESPONSE();

    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public RespBase<LoginDto> register (@RequestBody UserRegistrationDto userRegistrationDto) {

        return new ResponseVo().ok(userService.register(userRegistrationDto));

    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public RespBase<LoginDto> login (@RequestBody UserDto dto) {

        return new ResponseVo().ok(userService.login(dto));

    }

    @ApiOperation("修改密码")
    @PostMapping("/updatePwd")
    public RespBase updatePwd (@RequestAttribute(MvcConfig.SESSION_USER) Long uid,
                               @RequestBody UpdatePwdDto dto) {

        userService.updatePwd(uid, dto);

        return ResponseVo.OK_RESPONSE();

    }

    @ApiOperation("找回密码")
    @PostMapping("/passRecall")
    public RespBase passRecall (@RequestBody PassRecallDto dto) {

        userService.passRecall(dto);

        return ResponseVo.OK_RESPONSE();

    }

    @ApiOperation("设置个人信息")
    @PostMapping("/setUpPersonalInfo")
    public RespBase setUpPersonalInfo (@RequestAttribute(MvcConfig.SESSION_USER) Long uid,
                                       @RequestBody SetUpPersonalInfoDto setUpPersonalInfoDto) {

        userService.setUpPersonalInfo(uid, setUpPersonalInfoDto);

        return ResponseVo.OK_RESPONSE();
    }

    @ApiOperation("获取用户信息")
    @PostMapping("/getUserInfo")
    public RespBase<UserBo> getUserInfo(@RequestAttribute(MvcConfig.SESSION_USER) Long uid,
                                        @RequestBody UserInfoDto dto){

        return new ResponseVo().ok(userService.getUserInfo(uid, dto));
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public RespBase<UserBo> logout (@RequestAttribute(MvcConfig.SESSION_USER) Long uid){

        userService.logout(uid);

        return ResponseVo.OK_RESPONSE();
    }

}
