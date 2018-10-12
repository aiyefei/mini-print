package com.delicloud.app.miniprint.server.repository;


import com.delicloud.app.miniprint.core.entity.TFileModel;
import com.delicloud.platform.common.data.repository.MyRepository;

import java.util.List;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/7/17 09:23
 */
public interface FileModelRepository extends MyRepository<TFileModel, Long> {

    List<TFileModel> findByChecksum(String md5String);

}

