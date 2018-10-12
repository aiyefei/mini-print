package com.delicloud.app.miniprint.core.vo;

import lombok.Data;

/**
 * @Author: dy
 * @Description: 用户分页对象
 * @Date: 2018/9/14 18:45
 */
@Data
public class UserFollowPageVo {

    private Long uid;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 个性签名
     */
    private String IndividualitySignature;

    /**
     * 关注状态 1: 未关注, 2: 已关注, 3:互相关注
     */
    private Integer followStatus;

    /**
     * 是否是我
     */
    private boolean whetherMe;

}
