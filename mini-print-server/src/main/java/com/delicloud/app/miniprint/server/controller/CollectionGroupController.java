package com.delicloud.app.miniprint.server.controller;

import com.delicloud.app.miniprint.core.dto.AddGroupDto;
import com.delicloud.app.miniprint.core.dto.CreateGroupDto;
import com.delicloud.app.miniprint.core.dto.UpdateGroupNameDto;
import com.delicloud.app.miniprint.core.vo.CollectionGroupVo;
import com.delicloud.app.miniprint.core.vo.ResponseVo;
import com.delicloud.app.miniprint.server.config.MvcConfig;
import com.delicloud.app.miniprint.server.service.ICollectionGroupService;
import com.delicloud.platform.common.lang.bo.RespBase;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weibo/group")
public class CollectionGroupController {
    @Autowired
    private ICollectionGroupService collectionGroupService;

    @ApiOperation("创建分组")
    @PostMapping("/create")
    public RespBase createGroup(@RequestAttribute(MvcConfig.SESSION_USER) Long uid,
                                @RequestBody CreateGroupDto dto){

        collectionGroupService.createGroup(uid, dto);

        return ResponseVo.OK_RESPONSE();
    }

    @ApiOperation("微博分组操作")
    @RequestMapping("/operate")
    public RespBase operate (@RequestAttribute(MvcConfig.SESSION_USER) Long uid,
                             @RequestBody AddGroupDto dto){

        collectionGroupService.operate(uid, dto);

        return ResponseVo.OK_RESPONSE();
    }

    @ApiOperation("删除用户收藏分组")
    @RequestMapping("/delete")
    public RespBase deleteGroup(@RequestBody AddGroupDto dto){

        collectionGroupService.deleteGroup(dto);

        return ResponseVo.OK_RESPONSE();
    }

    @ApiOperation("修改分组名")
    @PostMapping("/update")
    public RespBase updateGroupName(@RequestAttribute(MvcConfig.SESSION_USER) Long uid,
                                    @RequestBody UpdateGroupNameDto dto){

        collectionGroupService.updateGroupName(uid, dto);

        return ResponseVo.OK_RESPONSE();
    }

    @ApiOperation("获取我的分组列表")
    @PostMapping("/getMyGroups")
    public RespBase<List<CollectionGroupVo>> getMyGroups (@RequestAttribute(MvcConfig.SESSION_USER) Long uid,
                                                          @RequestBody UpdateGroupNameDto dto){

        return new ResponseVo().ok(collectionGroupService.getMyGroups(uid, dto));
    }

}
