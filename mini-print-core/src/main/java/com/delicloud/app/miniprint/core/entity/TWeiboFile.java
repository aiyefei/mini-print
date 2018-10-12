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
@Entity(name= TWeiboFile.TABLE_NAME)
@DynamicInsert
@Table(appliesTo = TWeiboFile.TABLE_NAME, comment = "微博文件")
public class TWeiboFile extends BaseEntity {
    protected static final String TABLE_NAME = "tm_weibo_file";

    private Long createBy;

    @NotNull
    private Long createTime;

    @NotNull
    @Column(name = "wb_id", columnDefinition = "BIGINT(20) UNSIGNED COMMENT '微博ID'")
    private Long wbId;

    @Column(name = "file_type", columnDefinition = "VARCHAR(255) COMMENT 'image,project'")
    private String fileType;

    @NotNull
    @Column(name = "name", columnDefinition = "VARCHAR(255) COMMENT '文件名'")
    private String name;

    @NotNull
    @Column(name = "image_url", columnDefinition = "VARCHAR(255) COMMENT '预览图路径'")
    private String imageUrl;

    @NotNull
    @Column(name = "file_url", columnDefinition = "VARCHAR(255) COMMENT '文件路径'")
    private String fileUrl;

    @NotNull
    @Column(name = "size", columnDefinition = "BIGINT(20) COMMENT '文件大小'")
    private Long size;
}
