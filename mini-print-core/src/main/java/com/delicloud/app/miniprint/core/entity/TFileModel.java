package com.delicloud.app.miniprint.core.entity;

import com.delicloud.platform.common.data.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 网盘文件[新]
 * @author dongrui
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "t_file_model")
public class TFileModel extends BaseEntity {

    @NotNull
    @Column(name = "name", columnDefinition = "varchar(255) COMMENT '文件名称'")
    private String name;

    @NotNull
    @Column(name = "format", columnDefinition = "varchar(10) COMMENT '文件类型'")
    private String format;

    @NotNull
    @Column(name = "create_by", columnDefinition = "BIGINT(18) COMMENT '创建人'")
    private Long createBy;

    @NotNull
    @Column(name = "create_time", columnDefinition = "BIGINT(18) COMMENT '创建时间'")
    private Long createTime;

    @NotNull
    @Column(name = "absolute_path", columnDefinition = "varchar(255) COMMENT '文件绝对路径'")
    private String absolutePath;

    /**
     * 相对路径
     */
    @NotNull
    @Column(name = "relative_path", columnDefinition = "varchar(255) COMMENT '文件相对路径'")
    private String relativePath;

    /**
     * 文件大小
     */
    @Column(name = "size", columnDefinition = "bigint(20) COMMENT '文件相对路径'")
    private long size;

    /**
     * 校验和(输入流 md 加密后的字符串)
     */
    @NotNull
    @Column(name = "checksum", columnDefinition = "varchar(255) COMMENT '文件 md5'")
    private String checksum;

    @Column(name = "storage_id", columnDefinition = "varchar(255) COMMENT '存储 id'")
    private String storageId;
}
