package com.delicloud.app.miniprint.server.service;

import com.delicloud.app.miniprint.core.dto.AppVersionRequestDto;
import com.delicloud.app.miniprint.core.vo.PrinterFirmwareVo;


public interface IPrinterFirmwareService {
    /**
     * 获取最新固件
     */
    PrinterFirmwareVo getPrinterVersion(AppVersionRequestDto dto);
}
