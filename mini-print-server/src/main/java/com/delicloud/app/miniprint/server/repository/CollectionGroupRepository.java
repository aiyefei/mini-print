package com.delicloud.app.miniprint.server.repository;

import com.delicloud.app.miniprint.core.entity.TCollectionGroup;
import com.delicloud.platform.common.data.repository.MyRepository;

import java.util.List;


public interface CollectionGroupRepository extends MyRepository<TCollectionGroup,Long> {

    /**
     * 根据用户id和分组名查询分组是否存在
     * @param uid 用户 id
     * @param groupName 分组名
     * @return
     */
    TCollectionGroup findByUidAndGroupName(Long uid, String groupName);

    /**
     * 根据用户 id 查询用户分组列表
     * @param uid 用户 id
     * @return
     */
    List<TCollectionGroup> findByUid(Long uid);
}
