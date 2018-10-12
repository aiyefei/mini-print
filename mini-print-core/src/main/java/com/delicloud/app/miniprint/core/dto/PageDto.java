package com.delicloud.app.miniprint.core.dto;

import lombok.Data;

/**
 * @Author: dy
 * @Description: 分页入参对象
 * @Date: 2018/9/20 23:07
 */
@Data
public class PageDto {

    /**
     * 当前页: 默认 1
     */
    private Integer pageNo;

    /**
     * 一页大小: 默认 10
     */
    private Integer pageSize;

    public Integer getPageNo () {
        if (null == this.pageNo)
            return 1;
        else
            return this.pageNo;
    }

    public Integer getPageSize () {
        if (null == this.pageSize)
            return 10;
        else
            return this.pageSize;
    }

}
