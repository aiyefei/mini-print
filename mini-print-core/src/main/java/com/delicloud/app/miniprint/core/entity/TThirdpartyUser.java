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
@Entity(name= TThirdpartyUser.TABLE_NAME)
@DynamicInsert
//@Table(name = TUser.TABLE_NAME)
@Table(appliesTo = TThirdpartyUser.TABLE_NAME, comment = "第三方平台用户表")
public class TThirdpartyUser extends BaseEntity {

    protected static final String TABLE_NAME = "tm_thirdparty_user";

    public static String THIRDPARTY = "deli";

    private Long createBy;

    @NotNull
    private Long createTime;

    @NotNull
    @Column(name="uid", columnDefinition="BIGINT(18) UNSIGNED COMMENT '会员ID'")
    private Long uid;

    @NotNull
    @Column(name="thirdparty", columnDefinition="VARCHAR(16) COMMENT '第三方平台: deli'")
    private String thirdparty;

    @NotNull
    @Column(name="thirdparty_uid", columnDefinition="VARCHAR(32) COMMENT '第三方平台用户ID'")
    private String thirdpartyUid;

    @Column(name="thirdparty_nickname", columnDefinition="VARCHAR(128) COMMENT '第三方平台用户昵称'")
    private String thirdpartyNickname;

    // TODO 备注? 属性类型?
    @Column(name="thirdparty_user", columnDefinition="text COMMENT '第三方平台用户昵称'")
    private String thirdpartyUser;

}
