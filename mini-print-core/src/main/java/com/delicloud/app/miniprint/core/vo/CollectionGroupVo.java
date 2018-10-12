package com.delicloud.app.miniprint.core.vo;

import lombok.Data;

/**
 * @Author: dy
 * @Description: 收藏分组对象
 * @Date: 2018/9/14 18:45
 */
@Data
public class CollectionGroupVo {

    /**
     * 收藏分组 id
     */
    private long id;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 描述
     */
    private String groupRemark;

    /**
     * 分组内容数量
     */
    private Integer groupConetntCount;

}
