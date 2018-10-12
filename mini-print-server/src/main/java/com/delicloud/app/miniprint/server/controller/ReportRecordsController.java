package com.delicloud.app.miniprint.server.controller;

import com.delicloud.app.miniprint.core.dto.ReportUserDto;
import com.delicloud.app.miniprint.core.vo.ResponseVo;
import com.delicloud.app.miniprint.server.config.MvcConfig;
import com.delicloud.app.miniprint.server.service.IReportRecordsService;
import com.delicloud.platform.common.lang.bo.RespBase;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: dy
 * @Description: 用户举报控制器
 * @Date: 2018/9/14 17:38
 */
@RestController
@RequestMapping("/reportRecords")
public class ReportRecordsController {

    @Autowired
    private IReportRecordsService reportRecordsService;

    @ApiOperation("举报用户")
    @PostMapping("/report")
    public RespBase report(@RequestAttribute(MvcConfig.SESSION_USER) Long uid,
                           @RequestBody ReportUserDto reportUserDto){

        reportRecordsService.report(uid, reportUserDto);

        return ResponseVo.OK_RESPONSE();
    }

}
