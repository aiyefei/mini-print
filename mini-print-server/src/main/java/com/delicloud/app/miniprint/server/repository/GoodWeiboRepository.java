package com.delicloud.app.miniprint.server.repository;

import com.delicloud.app.miniprint.core.entity.TGoodWeibo;
import com.delicloud.platform.common.data.repository.MyRepository;

import java.util.List;

public interface GoodWeiboRepository extends MyRepository<TGoodWeibo,Long> {

    /**
     * 根据用户Id查找所有的被点赞的微博
     * @param uid 用户 id
     * @return
     */
    List<TGoodWeibo> findByUid(Long uid);

    TGoodWeibo findByUidAndWbId(Long uid, Long wbId);
}
