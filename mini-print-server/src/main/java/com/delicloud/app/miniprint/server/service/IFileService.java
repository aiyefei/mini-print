package com.delicloud.app.miniprint.server.service;

import com.delicloud.app.miniprint.core.entity.TFileModel;
import com.delicloud.app.miniprint.core.vo.FileRespVo;
import com.delicloud.app.miniprint.core.vo.FileVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * @Author: dy
 * @Description: 文件上传 service
 * @Date: 2018/8/31 16:16
 */
public interface IFileService {

    /**
     * 上传头像
     * @return
     */
    void upload(MultipartFile file, Long uid, String type);

//    List<FileVo> uploadFiles(MultipartFile[] files, String type, Long uid);

    TFileModel test(MultipartFile file, Long uid, String type);

    List<FileRespVo> uploadFiles(MultipartFile[] files, Long uid);
    
   
}
