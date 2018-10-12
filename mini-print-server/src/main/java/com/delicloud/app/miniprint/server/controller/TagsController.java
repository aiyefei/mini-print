package com.delicloud.app.miniprint.server.controller;

import com.delicloud.app.miniprint.core.dto.TagDto;
import com.delicloud.app.miniprint.core.entity.TTags;
import com.delicloud.app.miniprint.core.vo.ResponseVo;
import com.delicloud.app.miniprint.server.service.ITagsService;
import com.delicloud.platform.common.lang.bo.RespBase;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: dy
 * @Description: 标签控制器
 * @Date: 2018/9/14 17:38
 */
@RestController
@RequestMapping("/tag")
public class TagsController {

    @Autowired
    private ITagsService tagsService;

    @ApiOperation("获取标签列表")
    @PostMapping("/getList")
    public RespBase<List<TTags>> getListByType(@RequestBody TagDto dto){

        return new ResponseVo().ok(tagsService.getListByType(dto));
    }

}
