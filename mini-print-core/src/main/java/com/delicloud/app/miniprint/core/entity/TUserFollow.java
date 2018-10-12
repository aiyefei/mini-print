package com.delicloud.app.miniprint.core.entity;

import com.delicloud.platform.common.data.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name= TUserFollow.TABLE_NAME)
@DynamicInsert
@Table(appliesTo = TUserFollow.TABLE_NAME, comment = "用户关注表")
public class TUserFollow extends BaseEntity {
    protected static final String TABLE_NAME = "tm_user_follow";

    private Long createBy;

    @NotNull
    private Long createTime;

    @NotNull
    @Column(name = "from_uid", columnDefinition = "BIGINT(18) COMMENT '关注人'")
    private Long fromUid;

    @NotNull
    @Column(name = "to_uid", columnDefinition = "BIGINT(18) COMMENT '被关注人'" )
    private Long toUid;

    @NotNull
    @Column(name = "follow_status", columnDefinition = "TINYINT(1) COMMENT '关注状态 1: 已关注, 对方未关注 2: 未关注, 对方已关注, 3: 互相关注'" )
    private Integer followStatus;

}
