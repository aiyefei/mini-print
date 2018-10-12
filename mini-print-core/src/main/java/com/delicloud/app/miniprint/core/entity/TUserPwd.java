package com.delicloud.app.miniprint.core.entity;

import com.delicloud.platform.common.data.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * @Author: dy
 * @Description: 用户信息表
 * @Date: 2018/8/30 16:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name= TUserPwd.TABLE_NAME)
@DynamicInsert
//@Table(name = TUser.TABLE_NAME)
@Table(appliesTo = TUserPwd.TABLE_NAME, comment = "用户密码表")
public class TUserPwd extends BaseEntity {

    protected static final String TABLE_NAME = "tt_user_pwd";

    private Long createBy;

    @NotNull
    private Long createTime;

    @NotNull
    @Column(name="uid", columnDefinition="BIGINT(18) UNSIGNED COMMENT '会员ID'")
    private Long uid;

    @NotNull
    @Column(name="pwd_type", columnDefinition="CHAR(2) COMMENT '密码类型：登陆密码10、支付密码11'")
    private String pwdType;

    @NotNull
    @Column(name="cipher_type", columnDefinition="VARCHAR(8) COMMENT '加密类型：MD5、SHA256'")
    private String cipherType;

    @Column(name="cipher_key", columnDefinition="VARCHAR(16) COMMENT '加密key'")
    private String cipherKey;

    @NotNull
    @Column(name="password", columnDefinition="VARCHAR(256) COMMENT '密码铭文'")
    private String password;

}
