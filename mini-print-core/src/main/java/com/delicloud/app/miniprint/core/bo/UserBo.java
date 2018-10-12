package com.delicloud.app.miniprint.core.bo;

import com.delicloud.app.miniprint.core.entity.TCollectionGroup;
import lombok.Data;

import java.util.List;

/**
 * @Author: dy
 * @Description: 用户出参业务模型
 * @Date: 2018/8/30 16:25
 */
@Data
public class UserBo {

    private Long createBy;

    private String backgroundUrl;

    private Long createTime;

    private String avatarUrl;

    private String nickName;

    private String realName;

    private String region;

    private String mobile;

    private String gender;

    private String email;

    private String identityType;

    private String birthday;

    private String identityNo;

    private String fId;

    private String inviteCode;

    private String area;

    private String remark;

    private String status;

    private Long score;

    private Long follows;

    private Long followers;

    private String IndividualitySignature;

    /**
     * 关注状态 1: 已关注 2: 未关注 3: 互相关注 4: 自己
     */
    private Integer followStatus;

    /**
     * 用户分组列表
     */
    private List<TCollectionGroup> collectionGroups;

    /**
     * 用户微博数量
     */
    private Integer wbCount;

}
