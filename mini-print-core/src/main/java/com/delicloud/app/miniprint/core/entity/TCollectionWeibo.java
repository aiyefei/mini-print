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
@Entity(name= TCollectionWeibo.TABLE_NAME)
@DynamicInsert
@Table(appliesTo = TCollectionWeibo.TABLE_NAME, comment = "用户收藏的微博表")
public class TCollectionWeibo extends BaseEntity {

    protected static final String TABLE_NAME = "tl_collection_weibo";

    private Long createBy;

    @NotNull
    private Long createTime;

    @NotNull
    @Column(name = "uid", columnDefinition = "BIGINT(18) UNSIGNED COMMENT '会员ID'")
    private Long uid;

    @NotNull
    @Column(name = "cg_id", columnDefinition = "BIGINT(18) UNSIGNED COMMENT '收藏分组ID'")
    private Long cgId;

    @NotNull
    @Column(name = "wb_id", columnDefinition = "BIGINT(18) UNSIGNED COMMENT '微博ID'")
    private Long wbId;

    @Column(name = "wb_type", columnDefinition = "VARCHAR(6) COMMENT '微博类型:normal'")
    private String wbType;


}
