package com.delicloud.app.print.message.controller;

import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

public class TestTransferController {
    public RestTemplate restTemplate=new RestTemplate();
    private String severUrl="http://139.224.112.154:8080";

    @Test
    public void testTransfer(){
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        String filePath = "/Users/qiudanping/Desktop/tm_area_20170220.xlsx";
        FileSystemResource resource = new FileSystemResource(new File(filePath));
        postParameters.add("file", resource);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, headers);
        System.out.println(restTemplate.postForEntity(severUrl+"/v1.0/transfer/sync/tm_area_20170220.xlsx",requestEntity,String.class));
    }

    @Test
    public void testJSON(){
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, headers);
        System.out.println(restTemplate.postForEntity(severUrl+"/v1.0/transfer/test",requestEntity,String.class));
    }
}
