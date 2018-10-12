package com.delicloud.app.miniprint.core.entity;
import com.delicloud.app.miniprint.core.bo.UserBo;
import com.delicloud.platform.common.data.entity.BaseEntity;
import com.delicloud.platform.common.lang.util.MyFastBeanUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author: dy
 * @Description: 用户信息表
 * @Date: 2018/8/30 16:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name= TUser.TABLE_NAME)
@DynamicInsert
//@Table(name = TUser.TABLE_NAME)
@Table(appliesTo = TUser.TABLE_NAME, comment = "用户信息表")
public class TUser extends BaseEntity {

    protected static final String TABLE_NAME = "tm_user";

    private Long createBy;

    @NotNull
    private Long createTime;

    @Column(name="avatar_url", columnDefinition="VARCHAR(255) COMMENT '头像Url'")
    private String avatarUrl;

    @Column(name="background_url", columnDefinition="VARCHAR(255) COMMENT '背景图Url'")
    private String backgroundUrl;

    @Column(name="nick_name", columnDefinition="VARCHAR(55) COMMENT '昵称'")
    private String nickName;

    @Column(name="real_name", columnDefinition="VARCHAR(255) COMMENT '真实姓名'")
    private String realName;

    @Column(name="region", columnDefinition="VARCHAR(8) COMMENT '手机国际区号'")
    private String region;

    @NotNull
    @Column(name="mobile", columnDefinition="VARCHAR(16) COMMENT '手机号码'")
    private String mobile;

    @Column(name="gender", columnDefinition="VARCHAR(6) COMMENT '性别:male/男 female/女'")
    private String gender;

    @Column(name="email", columnDefinition="VARCHAR(64) COMMENT '邮箱'")
    private String email;

    @Column(name="identity_type", columnDefinition="CHAR(4) COMMENT '证件类型:'")
    private String identityType;

    @Column(name="birthday", columnDefinition="VARCHAR(64) COMMENT '生日'")
    private String birthday;

    @Column(name="identity_no", columnDefinition="VARCHAR(64) COMMENT '证件号码'")
    private String identityNo;

    @Column(name="f_id", columnDefinition="VARCHAR(64) COMMENT '手机标识'")
    private String fId;

    @Column(name="invite_code", columnDefinition="VARCHAR(12) COMMENT '邀请码'")
    private String inviteCode;

    @Column(name="area", columnDefinition="VARCHAR(255) COMMENT '地区'")
    private String area;

    @Column(name="remark", columnDefinition="VARCHAR(255) COMMENT '简介'")
    private String remark;

    @Column(name="status", columnDefinition="CHAR(1) default '' COMMENT '状态：0未激活、1正常、2冻结'")
    private String status;

    @NotNull
    @Column(name="score", columnDefinition="bigint(11) unsigned COMMENT '积分'")
    private Long score;

    @Column(name="follows", columnDefinition="bigint(11) unsigned default 0 COMMENT '关注数'")
    private Long follows;

    @Column(name="followers", columnDefinition="bigint(11) unsigned default 0 COMMENT '粉丝数'")
    private Long followers;

    @Column(name="Individuality_signature", columnDefinition="VARCHAR(255) COMMENT '个性签名'")
    private String IndividualitySignature;

    public UserBo toUserBo() {
        return MyFastBeanUtils.copyProperties(this, UserBo.class);
    }

}
