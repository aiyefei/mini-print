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
@Entity(name= TReportRecords.TABLE_NAME)
@DynamicInsert
@Table(appliesTo = TReportRecords.TABLE_NAME, comment = "举报记录表")
public class TReportRecords extends BaseEntity {
    protected static final String TABLE_NAME = "tm_report_records";

    private Long createBy;

    @NotNull
    private Long createTime;

    @Column(name = "report_uid", columnDefinition = "bigint(18) COMMENT '举报的用户的 id'")
    private Long reportUid;

    @NotNull
    @Column(name = "reported_uid", columnDefinition = "bigint(18) COMMENT '被举报的用户 id'")
    private Long reportedUid;

    @NotNull
    @Column(name = "report_tag_id", columnDefinition = "BIGINT(18) COMMENT '举报标签 id'")
    private Long reportTagId;

    @NotNull
    @Column(name = "report_content", columnDefinition = "varchar(255) COMMENT '举报内容'")
    private String reportContent;

}
