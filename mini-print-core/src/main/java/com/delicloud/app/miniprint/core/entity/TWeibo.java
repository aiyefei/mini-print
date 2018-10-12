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
@Entity(name= TWeibo.TABLE_NAME)
@DynamicInsert
@Table(appliesTo = TWeibo.TABLE_NAME, comment = "微博")
public class TWeibo extends BaseEntity {
    protected static final String TABLE_NAME = "tm_weibo";

    private Long createBy;

    @NotNull
    private Long createTime;

    @NotNull
    @Column(name = "uid", columnDefinition = "BIGINT(20) UNSIGNED COMMENT '会员ID'")
    private Long uid;

    @Column(name = "weibo_type", columnDefinition = "VARCHAR(6) COMMENT '微博类型:normal'")
    private String weiboType;

    @NotNull
    @Column(name = "title", columnDefinition = "VARCHAR(100) COMMENT '微博名称'")
    private String title;

    @NotNull
    @Column(name = "content", columnDefinition = "text COMMENT '微博内容'")
    private String content;

    @Column(name = "image", columnDefinition = "VARCHAR(255) COMMENT '微博内容（图片）'")
    private String image;

    @Column(name = "sort_no", columnDefinition = "BIGINT(20) COMMENT '排序'")
    private Long sortNo;

    @Column(name = "collections", columnDefinition = "BIGINT(11) UNSIGNED COMMENT '收藏数'")
    private Long collections;

    @Column(name = "views", columnDefinition = "BIGINT(11) UNSIGNED COMMENT '浏览数'")
    private Long views;

    @Column(name = "goods", columnDefinition = "BIGINT(11) UNSIGNED COMMENT '点赞数'")
    private Long goods;

    @Column(name = "downloads", columnDefinition = "BIGINT(11) UNSIGNED COMMENT '下载数'")
    private Long downloads;

    @Column(name = "can_share", columnDefinition = "TINYINT(1) COMMENT '是否能分享: 1能，0不能'")
    private Integer canShare;

    @NotNull
    @Column(name = "is_top", columnDefinition = "TINYINT(4) COMMENT '是否置顶: 1能，0不能'")
    private Integer isTop;

    @Column(name = "referees", columnDefinition = "VARCHAR(255) COMMENT '推荐人'")
    private String referees;

    @NotNull
    @Column(name = "status", columnDefinition = "CHAR(1) COMMENT '状态:1正常，0删除'")
    private String status;

    @NotNull
    @Column(name = "file_list_json", columnDefinition = "varchar(6144) COMMENT '文件列表的 json 字符串'")
    private String fileListJson;


}
