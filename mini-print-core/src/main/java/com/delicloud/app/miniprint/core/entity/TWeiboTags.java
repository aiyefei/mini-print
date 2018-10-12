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
@Entity(name= TWeiboTags.TABLE_NAME)
@DynamicInsert
@Table(appliesTo = TWeiboTags.TABLE_NAME,comment = "微博标签")
public class TWeiboTags extends BaseEntity {
    protected static final String TABLE_NAME = "tm_weibo_tags";

    private Long createBy;

    @NotNull
    private Long createTime;

    @NotNull
    @Column(name = "wb_id", columnDefinition = "BIGINT(20) UNSIGNED COMMENT '微博ID'")
    private Long wbId;

    @NotNull
    @Column(name = "tag_name", columnDefinition = "VARCHAR(255) COMMENT '标签名'")
    private String tagName;
}
