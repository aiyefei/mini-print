package com.delicloud.app.miniprint.core.dto;

import com.delicloud.app.miniprint.core.entity.TWeiboFile;
import lombok.Data;

import java.util.List;

/**
 * @Author: dy
 * @Description: 用户举报入参
 * @Date: 2018/9/4 15:40
 */
@Data
public class ReportUserDto {

    /**
     * 被举报用户 id
     */
    private Long reportedUid;

    /**
     * 举报标签 id
     */
    private Long reportTagId;

    /**
     * 举报内容
     */
    private String reportContent;

}
