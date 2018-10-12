package com.delicloud.app.miniprint.core.entity;

import com.delicloud.platform.common.data.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Table;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name= TPrinterFirmware.TABLE_NAME)
@DynamicInsert
@Table(appliesTo = TPrinterFirmware.TABLE_NAME, comment = "打印机固件")
public class TPrinterFirmware extends BaseEntity {
    protected static final String TABLE_NAME = "tt_printer_firmware";

    private Long createBy;

    private Long createTime;

    private String firmwareContext;

    private String firmwareName;

    private Long firmwareSize;

    private String firmwareUrl;

    private String firmwareVersion;

    private String printerModel;

    private String printerType;

    private String signKey;

    private Integer status;
}
