package com.delicloud.app.miniprint.server.repository;

import com.delicloud.app.miniprint.core.entity.TWeiboFile;
import com.delicloud.platform.common.data.repository.MyRepository;

import java.util.List;

public interface WeiboFileRepository extends MyRepository<TWeiboFile,Long> {

    /**
     * 根据微博 id 查询微博文件列表
     * @param wbId 微博 id
     * @return
     */
    List<TWeiboFile> findByWbId(Long wbId);
}
