package com.delicloud.app.miniprint.core.dto;

import lombok.Data;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/9/19 11:32
 */
@Data
public class WeiboDto {

    private Long cgId;
    private Long wbId;

    /**
     * 类型 1: 点赞 0: 取消点赞
     */
    private Integer type;

}
