package com.delicloud.app.miniprint.server.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.delicloud.app.miniprint.server.repository.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.delicloud.app.miniprint.core.bo.UserBo;
import com.delicloud.app.miniprint.core.dto.PassRecallDto;
import com.delicloud.app.miniprint.core.dto.SetUpPersonalInfoDto;
import com.delicloud.app.miniprint.core.dto.UpdatePwdDto;
import com.delicloud.app.miniprint.core.dto.UserDto;
import com.delicloud.app.miniprint.core.dto.UserInfoDto;
import com.delicloud.app.miniprint.core.dto.UserRegistrationDto;
import com.delicloud.app.miniprint.core.entity.TCollectionGroup;
import com.delicloud.app.miniprint.core.entity.TThirdpartyUser;
import com.delicloud.app.miniprint.core.entity.TUser;
import com.delicloud.app.miniprint.core.entity.TUserFollow;
import com.delicloud.app.miniprint.core.util.Md5Util;
import com.delicloud.app.miniprint.server.Exception.AppException;
import com.delicloud.app.miniprint.server.dto.LoginDto;
import com.delicloud.app.miniprint.server.service.IUserService;
import com.delicloud.app.miniprint.server.util.TokenUtil;
import com.delicloud.platform.cloudapp.gateway.sdk.DeliGatewayApiClient;
import com.delicloud.platform.cloudapp.gateway.sdk.api.bo.req.DeliSmsSendRequest;
import com.delicloud.platform.cloudapp.gateway.sdk.api.bo.req.DeliUserInfoQueryRequest;
import com.delicloud.platform.cloudapp.gateway.sdk.api.bo.req.DeliUserLoginRequest;
import com.delicloud.platform.cloudapp.gateway.sdk.api.bo.req.DeliUserPasswordResetRequest;
import com.delicloud.platform.cloudapp.gateway.sdk.api.bo.req.DeliUserPasswordUpdateRequest;
import com.delicloud.platform.cloudapp.gateway.sdk.api.bo.req.DeliUserRegisterRequest;
import com.delicloud.platform.cloudapp.gateway.sdk.api.bo.resp.DeliUserInfoQueryResponse;
import com.delicloud.platform.cloudapp.gateway.sdk.api.bo.resp.DeliUserLoginResponse;
import com.delicloud.platform.cloudapp.gateway.sdk.api.bo.resp.DeliUserRegisterResponse;
import com.delicloud.platform.cloudapp.gateway.sdk.bo.DeliResponse;
import com.delicloud.platform.cloudapp.gateway.sdk.consts.ESmsMode;
import com.delicloud.platform.common.lang.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/8/31 16:17
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThirdpartyUserRepository thirdpartyUserRepository;

    @Resource(name = "printerDeliCloudGatewayAppClient")
    private DeliGatewayApiClient deliCloudGatewayAppClient;

    @Autowired
    private CollectionGroupRepository collectionGroupRepository;

    @Autowired
    private UserFollowRepository userFollowRepository;

    @Autowired
    private WeiboRepository weiboRepository;

    @Override
    public void getVerifyCode(UserDto dto) {

        // 校验手机号格式
//        if (!CheckMobile.isMobileNO(mobile))
//            throw new AppException(100, "手机号格式不正确");
        String type = dto.getType();
        String mobile = dto.getMobile();
        String region = dto.getRegion();

        // 平台 sdk 短信发送511
        DeliSmsSendRequest request = new DeliSmsSendRequest();
        request.setMobile(mobile);
        if (null == region || "".equals(region)) region = "86";
        request.setRegion(region);
        if ("1".equals(type)) {
            request.setMode(ESmsMode.Register);
        } else {
            request.setMode(ESmsMode.Reset);
        }
        log.info("Mobile= {}  getVerifyCode param= {}", dto.getMobile(), JsonUtil.getJsonFromObject(request));
        DeliResponse<?> response = deliCloudGatewayAppClient.sendSmsMsg(request);
        log.info("Mobile= {}  getVerifyCode output= {}", dto.getMobile(), JsonUtil.getJsonFromObject(response));
        if (null == response)
            throw new AppException(-1, "服务器异常,请稍后再试");
        if (!response.isSuccess())
            throw new AppException(-1, response.getMsg());

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public LoginDto register(UserRegistrationDto userRegistrationDto) {

        checkUserRegistrationDto(userRegistrationDto);

        String region = userRegistrationDto.getRegion() == null ? "86" : userRegistrationDto.getRegion();


        // 平台 sdk 用户注册 512
        DeliUserRegisterRequest request_ = new DeliUserRegisterRequest();
        request_.setMobile(userRegistrationDto.getMobile());
        request_.setRegion(region);
        request_.setPassword(Md5Util.encode(userRegistrationDto.getPassword()));
        request_.setVerifyCode(userRegistrationDto.getVerificationCode());
        log.info("Mobile = {} , VerificationCode = {} register param= {}", userRegistrationDto.getMobile(), userRegistrationDto.getVerificationCode() ,JsonUtil.getJsonFromObject(request_));
        DeliResponse<DeliUserRegisterResponse> response_ = deliCloudGatewayAppClient.registerUser(request_);
        log.info("Mobile = {} , VerificationCode = {} register output= {}", userRegistrationDto.getMobile(), userRegistrationDto.getVerificationCode() ,JsonUtil.getJsonFromObject(response_));
        if (null == response_)
            throw new AppException(-1, "服务器异常,请稍后再试");
        if (!response_.isSuccess())
            throw new AppException(-1, response_.getMsg());

        return doLogin(userRegistrationDto.getMobile(), userRegistrationDto.getPassword(), region);
    }

    /**
     * 校验入参
     * @param userRegistrationDto 用户注册入参
     * @return
     */
    private void checkUserRegistrationDto(UserRegistrationDto userRegistrationDto) {

        String mobile = userRegistrationDto.getMobile();

        String fId = userRegistrationDto.getFId();

        String password = userRegistrationDto.getPassword();

        String verificationCode = userRegistrationDto.getVerificationCode();

        String region = userRegistrationDto.getRegion();

        if (StringUtils.isBlank(mobile))
            throw new AppException(-1, "手机号不能为空");

        if (StringUtils.isBlank(fId))
            //throw new AppException(-1, "设备标识不能为空");

        if (StringUtils.isBlank(password))
            throw new AppException(-1, "用户密码不能为空");

        if (StringUtils.isBlank(verificationCode))
            throw new AppException(-1, "验证码不能为空");

        if (StringUtils.isBlank(region))    // 可选，手机号区域，默认86
            userRegistrationDto.setRegion("86");

        // 校验手机号格式
//        if (!CheckMobile.isMobileNO(mobile))
//            throw new AppException(100, "手机号格式不正确");

        TUser byMobile = userRepository.findByMobile(mobile);
        if (null != byMobile)
            throw new AppException(-1, "该手机号已经被注册");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public LoginDto login(UserDto dto) {
        String mobile = dto.getMobile();
        String password = dto.getPassword();
        String region = dto.getRegion();

        checkLoginParam(mobile, password);

        if (StringUtils.isBlank(region))
            region = "86";

        return doLogin(mobile, password, region);
    }

    private LoginDto doLogin(String mobile, String password, String region) {
            // 用户登录 515
            DeliUserLoginRequest request = new DeliUserLoginRequest();
            request.setMobile(mobile);
            request.setPassword(Md5Util.encode(password));
            request.setRegion(region == null ? "86" : region);
            log.info("Mobile= {}  login param= {}", mobile, JsonUtil.getJsonFromObject(request));

            DeliResponse<DeliUserLoginResponse> response = deliCloudGatewayAppClient.userLogin(request);
            log.info("Mobile= {}  login output= {}", mobile, JsonUtil.getJsonFromObject(response));

            if (null == response)
                throw new AppException(-1,"服务器异常,请稍后再试");
            if (!response.isSuccess())
                throw new AppException(-1, response.getMsg());

            String userId = response.getData().getUserId();
            String token = response.getData().getToken();

        try {
            if(StringUtils.isBlank(userId))
            	throw new Exception("平台异常");

            if(StringUtils.isBlank(token))
            	throw new Exception("平台异常");

            Long uid = null ;
            TThirdpartyUser tThirdpartyUser = thirdpartyUserRepository.findByThirdpartyUidAndThirdparty(userId, TThirdpartyUser.THIRDPARTY);
            if (tThirdpartyUser == null) {
                // 用户信息查询 517
                DeliUserInfoQueryRequest requestQuery = new DeliUserInfoQueryRequest();
                requestQuery.setMobile(mobile);
                requestQuery.setRegion("86");
                log.info("Mobile= {}  queryUserInfo param= {}", mobile, JsonUtil.getJsonFromObject(request));
                DeliResponse<DeliUserInfoQueryResponse> responseQuery = deliCloudGatewayAppClient.queryUserInfo(requestQuery);
                log.info("Mobile= {}  queryUserInfo output= {}", mobile, JsonUtil.getJsonFromObject(response));
                TUser tUser = new TUser();
                tUser.setMobile(mobile);
                tUser.setRegion("86");
                long currentTimeMillis = System.currentTimeMillis();
                tUser.setCreateTime(currentTimeMillis);
                tUser.setUpdateTime(currentTimeMillis);
                tUser.setScore(0l);
                tUser.setBackgroundUrl("https://test-delicloud.oss-cn-shanghai.aliyuncs.com/oss-1539152599056-290919.png");
                tUser.setFollows(0L);
                tUser.setStatus("1");
                tUser.setFollowers(0L);
                if (responseQuery.isSuccess())
                    tUser.setAvatarUrl(responseQuery.getData().getAvatar() == null ? "" : responseQuery.getData().getAvatar());
                userRepository.save(tUser);

                tThirdpartyUser = new TThirdpartyUser();
                tThirdpartyUser.setCreateTime(currentTimeMillis);
                tThirdpartyUser.setUpdateTime(currentTimeMillis);
                tThirdpartyUser.setThirdparty(TThirdpartyUser.THIRDPARTY);
                tThirdpartyUser.setThirdpartyUid(userId);
                tThirdpartyUser.setUid(tUser.getId());
                if (responseQuery.isSuccess())
                    tThirdpartyUser.setThirdpartyNickname(responseQuery.getData().getName() == null ? "" : responseQuery.getData().getName());
                thirdpartyUserRepository.save(tThirdpartyUser);

                // 新增默认分组
                TCollectionGroup collectionGroup = new TCollectionGroup();
                collectionGroup.setUid(tUser.getId());
                collectionGroup.setCreateTime(currentTimeMillis);
                collectionGroup.setUpdateTime(currentTimeMillis);
                collectionGroup.setGroupName("默认分组");
                collectionGroup.setGroupRemark("默认分组");
                collectionGroup.setContentCount(0);
                collectionGroupRepository.save(collectionGroup);
                uid = tUser.getId();

            }else {
            	uid = tThirdpartyUser.getUid();
            }
            if(uid == null) {
            	throw new Exception("数据异常");
            }
            LoginDto loginDto = new LoginDto();
            loginDto.setUid(uid);
            loginDto.setToken(token);
            return loginDto;

        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(-200, "服务器异常,请稍后再试");
        }
    }

    private void checkLoginParam(String mobile, String password) {

        if (StringUtils.isBlank(mobile))
            throw new AppException(-1, "手机号不能为空");

        if (StringUtils.isBlank(password))
            throw new AppException(-1, "密码不能为空");

        // 校验手机号格式
//        if (!CheckMobile.isMobileNO(mobile))
//            throw new AppException(100, "手机号格式不正确");

    }

    @Override
    public void updatePwd(Long uid, UpdatePwdDto dto) {
        String newPwd = dto.getNewPwd();
//        String sureNewPwd = dto.getSureNewPwd();
        String beforePwd = dto.getBeforePwd();

        String md5Pwd = Md5Util.encode(beforePwd);

        checkUpdatePwdParam(uid, beforePwd, newPwd);

        TThirdpartyUser thirdpartyUser = thirdpartyUserRepository.findByUid(uid);
        if (null == thirdpartyUser)
            throw new AppException(-1, "用户信息有误");

        // 平台 sdk 用户密码修改514
        DeliUserPasswordUpdateRequest request = new DeliUserPasswordUpdateRequest();
        request.setUserId(thirdpartyUser.getThirdpartyUid());
        request.setPassword(md5Pwd);
        request.setNewPassword(Md5Util.encode(newPwd));
        log.info("uid= {}  updatePwd param= {}", uid, JsonUtil.getJsonFromObject(request));
        DeliResponse<?> response = deliCloudGatewayAppClient.updateUserPassword(request);
        log.info("uid= {}  updatePwd output= {}", uid, JsonUtil.getJsonFromObject(response));
        if (null == response)
            throw new AppException(-1,"服务器异常，请稍后重试");
        if (!response.isSuccess())
            throw new AppException(-2, response.getMsg());
    }

    private void checkUpdatePwdParam(Long uid, String beforePwd, String newPwd) {
        if (null == uid)
            throw new AppException(-1, "用户 id 不能为空");

        if (StringUtils.isBlank(beforePwd))
            throw new AppException(-1, "旧密码不能为空");

        if (StringUtils.isBlank(newPwd))
            throw new AppException(-1, "新密码不能为空");

        if (newPwd.equals(beforePwd))
            throw new AppException(-1, "新密码不能与旧密码相同");

//        if (StringUtils.isBlank(sureNewPwd))
//            throw new AppException(-1, "确认密码不能为空");
//
//        if (!newPwd.equals(sureNewPwd))
//            throw new AppException(102, "两次新密码不一致");
    }

    @Override
    public void passRecall(PassRecallDto dto) {
        String mobile = dto.getMobile();
        String password = dto.getPassword();
        String verificationCode = dto.getVerificationCode();
        String region = dto.getRegion();

        checkPassRecallParam(mobile, password, verificationCode);
        if (StringUtils.isBlank(region))
            region = "86";

        // 平台 sdk 用户密码重置513
        DeliUserPasswordResetRequest request = new DeliUserPasswordResetRequest();
        request.setMobile(mobile);
        request.setPassword(Md5Util.encode(password));
        request.setRegion(region);
        request.setVerifyCode(verificationCode);
        log.info("mobile= {} , verificationCode = {}  passRecall param= {}", dto.getMobile(), dto.getVerificationCode()  ,JsonUtil.getJsonFromObject(request));
        DeliResponse<?> response = deliCloudGatewayAppClient.resetUserPassword(request);
        log.info("mobile= {} , verificationCode = {}  passRecall output= {}", dto.getMobile(), dto.getVerificationCode()  ,JsonUtil.getJsonFromObject(response));
        if (null == response)
            throw new AppException(-1,"服务器异常，请稍后重试");
        if (!response.isSuccess())
            throw new AppException(-2, response.getMsg());

    }

    private void checkPassRecallParam(String mobile, String password, String verificationCode) {
        if (StringUtils.isBlank(mobile))
            throw new AppException(-1, "手机号不能为空");

        if (StringUtils.isBlank(password))
            throw new AppException(-1, "用户密码不能为空");

        if (StringUtils.isBlank(verificationCode))
            throw new AppException(-1, "验证码不能为空");

        // 校验手机号格式
//        if (!CheckMobile.isMobileNO(mobile))
//            throw new AppException(100, "手机号格式不正确");
    }

    @Override
    public void setUpPersonalInfo(Long uid, SetUpPersonalInfoDto setUpPersonalInfoDto) {

        if (null == uid)
            throw new AppException(-1, "用户 id 不能为空");

        boolean needUpdate = false;
        TUser user = userRepository.findOne(uid);
        if (!StringUtils.isBlank(setUpPersonalInfoDto.getArea())) {
            user.setArea(setUpPersonalInfoDto.getArea());
            needUpdate = true;
        }

        if (!StringUtils.isBlank(setUpPersonalInfoDto.getAvatarUrl())) {
            user.setAvatarUrl(setUpPersonalInfoDto.getAvatarUrl());
            needUpdate = true;
        }

        if (!StringUtils.isBlank(setUpPersonalInfoDto.getGender())) {
            user.setGender(setUpPersonalInfoDto.getGender());
            needUpdate = true;
        }

        if (!StringUtils.isBlank(setUpPersonalInfoDto.getIndividualitySignature())) {
            if (setUpPersonalInfoDto.getIndividualitySignature().length() > 30) {
                throw new AppException(-1, "个性签名不能超过 30 个字符");
            }
            user.setIndividualitySignature(setUpPersonalInfoDto.getIndividualitySignature());
            needUpdate = true;
        }

        if (!StringUtils.isBlank(setUpPersonalInfoDto.getNickName())) {
            user.setNickName(setUpPersonalInfoDto.getNickName());
            needUpdate = true;
        }

        if (!StringUtils.isBlank(setUpPersonalInfoDto.getBirthday())) {
            user.setBirthday(setUpPersonalInfoDto.getBirthday());
            needUpdate = true;
        }

        if (needUpdate) {
            user.setUpdateTime(System.currentTimeMillis());
            userRepository.save(user);
        }
    }

    @Override
    public UserBo getUserInfo(Long uid, UserInfoDto dto) {

/*        Long checkedUid = dto.getCheckedUid();

        if (null == checkedUid)
            checkedUid = uid;*/
    	
    	Long checkedUid = dto.getCheckedUid();
    	if(null == checkedUid || 0 == checkedUid) {
    		log.info("checkedUid不能为空");
    		throw new AppException(-1, "服务器异常，请稍后重试");
    	}

        TUser tUser = userRepository.findOne(dto.getCheckedUid());
        if (null == tUser)
            throw new AppException(-1, "用户信息不存在");

        UserBo userBo = tUser.toUserBo();

        List<TCollectionGroup> collectionGroups = collectionGroupRepository.findByUid(checkedUid);
        userBo.setCollectionGroups(collectionGroups);

        if (!uid.equals(checkedUid)) { // 查询选中的用户id和当前用户的关注状态
            TUserFollow userFollow = userFollowRepository.findByFromUidAndToUid(uid, checkedUid);
            if (null != userFollow) {
                Integer followStatus = userFollow.getFollowStatus();
                userBo.setFollowStatus(followStatus);
            } else {
                userBo.setFollowStatus(3);
            }
        } else {
            userBo.setFollowStatus(4);
        }

         // 获取用户微博数量
        BigInteger count = weiboRepository.findCount(checkedUid);
        userBo.setWbCount(Integer.parseInt(count.toString()));
        return userBo;
    }

    @Override
    public void logout(Long uid) {
        // 删除该用户 id 对应的 token 信息
        TokenUtil tokenUtil = new TokenUtil();
        tokenUtil.deleteToken(uid);
    }

}
