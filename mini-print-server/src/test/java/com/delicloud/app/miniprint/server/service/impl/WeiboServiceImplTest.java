package com.delicloud.app.miniprint.server.service.impl;

import com.delicloud.app.miniprint.core.dto.QueryWeiboPageDto;
import com.delicloud.app.miniprint.server.service.IWeiboService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jws.WebService;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class WeiboServiceImplTest {

    @Autowired
    IWeiboService iWeiboService;

    @Test
    public void queryAllWeibo() {
        QueryWeiboPageDto dto = new QueryWeiboPageDto();
        dto.setCheckedUid(499880238277197824l);
        dto.setType("follow");
        Long uid = 499880238277197824l;

        log.info("ss = {} ",iWeiboService.queryAllWeibo(uid,dto));

    }

    @Test
    public void queryAllWeiboByUid() {
        QueryWeiboPageDto dto = new QueryWeiboPageDto();
        dto.setCheckedUid(499880238277197824l);
       log.info("sss= {}",iWeiboService.queryAllWeiboByUid(dto));
    }

    @Test
    public void queryAllWeiboByCgId() {
        QueryWeiboPageDto dto = new QueryWeiboPageDto();
        dto.setCgId(499880953246646272l);
        Long uid = 499880238277197824l;

       System.out.println(iWeiboService.queryAllWeiboByCgId(uid,dto));



    }
}