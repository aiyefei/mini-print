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
@Entity(name= TTags.TABLE_NAME)
@DynamicInsert
@Table(appliesTo = TTags.TABLE_NAME, comment = "标签表")
public class TTags extends BaseEntity {
    protected static final String TABLE_NAME = "tm_tags";

    private Long createBy;

    @NotNull
    private Long createTime;

    @Column(name = "tag_type", columnDefinition = "VARCHAR(6) COMMENT '标签类型: weibo/微博推荐  report/举报标签'")
    private String tagType;

    @NotNull
    @Column(name = "tag_name", columnDefinition = "VARCHAR(100) COMMENT '标签名称'")
    private String tagName;

    @NotNull
    @Column(name = "sort_no", columnDefinition = "BIGINT(20) COMMENT '排序'")
    private Long sortNo;

}
