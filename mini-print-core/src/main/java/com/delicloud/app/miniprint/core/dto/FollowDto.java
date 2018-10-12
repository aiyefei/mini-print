package com.delicloud.app.miniprint.core.dto;

import lombok.Data;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/9/19 11:23
 */
@Data
public class FollowDto {

    /**
     * 选择关注/取消关注的用户 id
     */
    private Long checkedUid;
}
