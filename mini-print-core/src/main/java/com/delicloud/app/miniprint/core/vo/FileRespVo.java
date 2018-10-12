package com.delicloud.app.miniprint.core.vo;

import lombok.Data;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/9/23 18:57
 */
@Data
public class FileRespVo {

    private String type;

    private String projectUrl;

    private String imageUrl;

    private Long projectSize;

    private Long imageSize;

    private String fileName;
}
