package com.delicloud.app.miniprint.core.vo;

import lombok.Data;

@Data
public class PrinterFirmwareVo {

    /**
     * 固件名称
     */
    private String firmwareName;

    /**
     * 固件大小
     */
    private Long firmwareSize;

    /**
     * 固件地址
     */
    private String firmwareUrl;

    /**
     * 固件版本
     */
    private String firmwareVersion;

    /**
     * 打印机模型
     */
    private String printerModel;

    /**
     * 打印机型号
     */
    private String printerType;

    /**
     * 校验MD5
     */
    private String firmwareMD5;

    private String signKey;

    private Integer status;
}
