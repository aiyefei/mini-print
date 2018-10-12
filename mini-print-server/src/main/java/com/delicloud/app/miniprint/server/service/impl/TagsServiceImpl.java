package com.delicloud.app.miniprint.server.service.impl;

import com.delicloud.app.miniprint.core.dto.TagDto;
import com.delicloud.app.miniprint.core.entity.TTags;
import com.delicloud.app.miniprint.server.Exception.AppException;
import com.delicloud.app.miniprint.server.repository.TagsRepository;
import com.delicloud.app.miniprint.server.service.ITagsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: dy
 * @Description: 微博业务层
 * @Date: 2018/8/31 16:17
 */
@Service
@Slf4j
public class TagsServiceImpl implements ITagsService {

    @Autowired
    private TagsRepository tagsRepository;

    @Override
    public List<TTags> getListByType(TagDto dto) {
        String type = dto.getType();
        if (null == type || "".equals(type))
            throw new AppException(-1, "标签类型不能为空");
        return tagsRepository.findByTagType(type);
    }
}
