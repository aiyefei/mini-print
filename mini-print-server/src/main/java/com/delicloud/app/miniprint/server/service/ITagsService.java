package com.delicloud.app.miniprint.server.service;

import com.delicloud.app.miniprint.core.dto.TagDto;
import com.delicloud.app.miniprint.core.entity.TTags;

import java.util.List;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/8/31 16:16
 */
public interface ITagsService {

    List<TTags> getListByType(TagDto dto);
}
