package com.delicloud.app.miniprint.server.service.impl;

import com.delicloud.app.miniprint.core.dto.ReportUserDto;
import com.delicloud.app.miniprint.core.entity.TReportRecords;
import com.delicloud.app.miniprint.core.entity.TTags;
import com.delicloud.app.miniprint.core.entity.TUser;
import com.delicloud.app.miniprint.server.Exception.AppException;
import com.delicloud.app.miniprint.server.repository.ReportRecordsRepository;
import com.delicloud.app.miniprint.server.repository.TagsRepository;
import com.delicloud.app.miniprint.server.repository.UserRepository;
import com.delicloud.app.miniprint.server.service.IReportRecordsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: dy
 * @Description: 举报用户业务层
 * @Date: 2018/8/31 16:17
 */
@Service
@Slf4j
public class ReportRecordsServiceImpl implements IReportRecordsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private ReportRecordsRepository reportRecordsRepository;

    @Override
    public void report(Long uid, ReportUserDto reportUserDto) {

        cheReportParam(reportUserDto);

        TUser user = userRepository.findByIdAndStatus(uid, "1");
        if (null == user)
            throw new AppException(-1, "举报用户信息不存在");

        Long reportedUid = reportUserDto.getReportedUid();
        TUser reportedUser = userRepository.findByIdAndStatus(reportedUid, "1");
        if (null == reportedUser)
            throw new AppException(-1, "被举报用户信息不存在或已被删除");

        Long reportTagId = reportUserDto.getReportTagId();
        TTags tTags = tagsRepository.findOne(reportTagId);
        if (null == tTags)
            throw new AppException(-1, "微博标签信息不存在");

        try {
            TReportRecords reportRecords = new TReportRecords();
            long currentTimeMillis = System.currentTimeMillis();
            reportRecords.setCreateTime(currentTimeMillis);
            reportRecords.setUpdateTime(currentTimeMillis);
            reportRecords.setCreateBy(uid);
            reportRecords.setReportUid(uid);
            reportRecords.setReportedUid(reportedUid);
            reportRecords.setReportContent(reportUserDto.getReportContent());
            reportRecords.setReportTagId(reportTagId);
            reportRecordsRepository.save(reportRecords);
        } catch (Exception e) {
            throw new AppException(-200, "举报失败");
        }
    }

    private void cheReportParam(ReportUserDto reportUserDto) {

        if (null == reportUserDto.getReportedUid())
            throw new AppException(-1, "被举报用户 id 不能为空");

        if (null == reportUserDto.getReportTagId())
            throw new AppException(-1, "举报标签 id 不能为空");

        if (null == reportUserDto.getReportContent())
            throw new AppException(-1, "举报内容不能为空");

        if (reportUserDto.getReportContent().length() > 100)
            throw new AppException(-1, "举报内容不得超过 100 个字");

    }
}
