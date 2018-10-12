package com.delicloud.app.miniprint.server.repository;

import com.delicloud.app.miniprint.core.entity.TCollectionWeibo;
import com.delicloud.platform.common.data.repository.MyRepository;

import java.util.List;

public interface CollectionWeiboRepository extends MyRepository<TCollectionWeibo,Long> {

    /**
     * 根据用户id, 微博id 查询用户收藏微博
     * @param uid 用户 id
     * @param wbId 微博 id
     * @return
     */
    TCollectionWeibo findByUidAndWbId(Long uid, Long wbId);

    List<TCollectionWeibo> findByCgId(Long cgId);
}
