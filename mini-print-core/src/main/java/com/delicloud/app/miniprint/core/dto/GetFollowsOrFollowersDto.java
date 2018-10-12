package com.delicloud.app.miniprint.core.dto;

import lombok.Data;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/9/19 11:17
 */
@Data
public class GetFollowsOrFollowersDto extends PageDto {

    private Long checkedUid;

    private Integer type;

}
