package com.delicloud.app.miniprint.server.service;

import com.delicloud.app.miniprint.core.dto.AddGroupDto;
import com.delicloud.app.miniprint.core.dto.CreateGroupDto;
import com.delicloud.app.miniprint.core.dto.UpdateGroupNameDto;
import com.delicloud.app.miniprint.core.vo.CollectionGroupVo;

import java.util.List;

public interface ICollectionGroupService {

    /**
     * 创建分组
     * @param dto 入参
     */
    void createGroup(Long uid, CreateGroupDto dto);

    /**
     * 加入分组
     * @param dto 入参
     */
    void operate(Long uid, AddGroupDto dto);

    /**
     * 删除用户收藏分组
     * @param dto 入参
     */
    void deleteGroup(AddGroupDto dto);

    /**
     * 重命名
     * @param dto 入参
     */
    void updateGroupName(Long uid, UpdateGroupNameDto dto);

    List<CollectionGroupVo> getMyGroups(Long uid, UpdateGroupNameDto dto);

}
