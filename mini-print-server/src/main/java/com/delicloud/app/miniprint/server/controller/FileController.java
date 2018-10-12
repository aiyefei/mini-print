package com.delicloud.app.miniprint.server.controller;

import com.delicloud.app.miniprint.core.entity.TFileModel;
import com.delicloud.app.miniprint.core.vo.FileRespVo;
import com.delicloud.app.miniprint.core.vo.FileVo;
import com.delicloud.app.miniprint.core.vo.ResponseVo;
import com.delicloud.app.miniprint.server.config.MvcConfig;
import com.delicloud.app.miniprint.server.service.IFileService;
import com.delicloud.platform.common.lang.bo.RespBase;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * @Author: dy
 * @Description: 文件上传控制器
 * @Date: 2018/9/14 17:38
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private IFileService fileService;

    @ApiOperation("修改头像/背景图片")
    @PostMapping("/upload")
    public RespBase upload(@RequestAttribute(MvcConfig.SESSION_USER) Long uid,
                           @RequestParam("file") MultipartFile file,
                           @RequestParam("type") String type){
        fileService.upload(file, uid, type);
        return ResponseVo.OK_RESPONSE();
    }

    @ApiOperation("上传文件")
    @PostMapping("/uploadFiles")
    public RespBase<List<FileRespVo>> uploadFiles(@RequestAttribute(MvcConfig.SESSION_USER) Long uid,
                                                  @RequestParam("files") MultipartFile[] files){

        return new ResponseVo().ok(fileService.uploadFiles(files, uid));
    }

}
