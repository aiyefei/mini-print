package com.delicloud.app.miniprint.server.service.impl;

import com.delicloud.app.miniprint.server.repository.GoodWeiboRepository;
import com.delicloud.app.miniprint.server.repository.WeiboRepository;
import com.delicloud.app.miniprint.server.service.IGoodWeiboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodWeiboServiceImpl implements IGoodWeiboService {
    @Autowired
    private GoodWeiboRepository goodWeiboRepository;

    @Autowired
    private WeiboRepository weiboRepository;

    @Override
    public void goodWeiBo(String uid, String touid, String weiboid) {

    }
}
