package com.delicloud.app.miniprint.core.entity;

import com.delicloud.platform.common.data.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name= TGoodWeibo.TABLE_NAME)
@DynamicInsert
@Table(appliesTo = TGoodWeibo.TABLE_NAME, comment = "用户点赞的微博")
public class TGoodWeibo extends BaseEntity {
    protected static final String TABLE_NAME = "tl_good_weibo";

    private Long createBy;

    @NotNull
    private Long createTime;

    @NotNull
    @Column(name = "uid", columnDefinition = "INT(11) UNSIGNED COMMENT '会员ID'")
    private Long uid;

    @NotNull
    @Column(name = "wb_id", columnDefinition = "BIGINT(20) UNSIGNED COMMENT '微博ID'")
    private Long wbId;

    @Column(name = "wb_type", columnDefinition = "VARCHAR(6) COMMENT '微博类型:normal'")
    private String wbType;

}
