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
@Entity(name= TCollectionGroup.TABLE_NAME)
@DynamicInsert
@Table(appliesTo = TCollectionGroup.TABLE_NAME, comment = "用户收藏分组表")
public class TCollectionGroup extends BaseEntity {
    protected static final String TABLE_NAME = "tl_collection_group";

    private Long createBy;

    @NotNull
    private Long createTime;

    @NotNull
    @Column(name = "uid",columnDefinition = "BIGINT(18) UNSIGNED COMMENT '会员ID'")
    private Long uid;

    @NotNull
    @Column(name = "group_name",columnDefinition = "VARCHAR(255) UNSIGNED COMMENT '分组名称'")
    private String groupName;

    @Column(name = "group_remark",columnDefinition = "VARCHAR(255) COMMENT '描述'")
    private String groupRemark;

    @Column(name = "content_count",columnDefinition = "int(8) COMMENT '内容数量' default 0")
    private Integer contentCount;

}
