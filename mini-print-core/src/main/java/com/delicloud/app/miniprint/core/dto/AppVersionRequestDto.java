package com.delicloud.app.miniprint.core.dto;

import lombok.Data;

@Data
public class AppVersionRequestDto {

    /**
     * 设备类型
     */
    private String type;

    /**
     * 版本号
     */
    private String version;

}
