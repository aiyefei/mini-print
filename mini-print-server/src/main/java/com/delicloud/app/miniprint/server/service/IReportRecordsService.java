package com.delicloud.app.miniprint.server.service;

import com.delicloud.app.miniprint.core.dto.ReportUserDto; /**
 * @Author: dy
 * @Description:
 * @Date: 2018/8/31 16:16
 */
public interface IReportRecordsService {

    void report(Long uid, ReportUserDto reportUserDto);
}
