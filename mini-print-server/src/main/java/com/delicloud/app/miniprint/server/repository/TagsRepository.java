package com.delicloud.app.miniprint.server.repository;

import com.delicloud.app.miniprint.core.entity.TTags;
import com.delicloud.platform.common.data.repository.MyRepository;

import java.util.List;

public interface TagsRepository extends MyRepository<TTags,Long> {
    /**
     * 根据标签类型获取标签列表
     * @param type 标签类型
     * @return
     */
    List<TTags> findByTagType(String type);
}
