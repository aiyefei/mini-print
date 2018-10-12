package com.delicloud.app.miniprint.core.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/9/19 11:30
 */
@Data
public class CreateGroupDto {

    private String groupName;
    private String groupRemark;

}
