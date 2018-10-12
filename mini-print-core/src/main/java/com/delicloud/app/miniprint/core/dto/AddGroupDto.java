package com.delicloud.app.miniprint.core.dto;

import lombok.Data;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/9/19 11:32
 */
@Data
public class AddGroupDto {

    private Long cgId;
    private Long wbId;

    /**
     * 操作类型 1: 加入分组 2: 取消分组, 3: 移动分组
     */
    private Integer type;
}
