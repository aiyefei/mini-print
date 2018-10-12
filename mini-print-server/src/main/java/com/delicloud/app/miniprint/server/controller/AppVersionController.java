package com.delicloud.app.miniprint.server.controller;

import com.delicloud.app.miniprint.core.dto.AppVersionRequestDto;
import com.delicloud.app.miniprint.core.vo.PrinterFirmwareVo;
import com.delicloud.app.miniprint.core.vo.ResponseVo;
import com.delicloud.app.miniprint.server.service.IPrinterFirmwareService;
import com.delicloud.platform.common.lang.bo.RespBase;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class AppVersionController {
    @Autowired
    IPrinterFirmwareService printerFirmwareService;

    @ApiOperation("app版本信息")
    @PostMapping("/getVersion")
    public RespBase<PrinterFirmwareVo> printers(@RequestBody AppVersionRequestDto dto){

        return new ResponseVo().ok(printerFirmwareService.getPrinterVersion(dto));
    }
}
