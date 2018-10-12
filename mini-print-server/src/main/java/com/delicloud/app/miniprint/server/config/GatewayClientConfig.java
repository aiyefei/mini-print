package com.delicloud.app.miniprint.server.config;

import com.delicloud.platform.cloudapp.gateway.sdk.DeliCloudAppClient;
import com.delicloud.platform.cloudapp.gateway.sdk.DeliGatewayApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@Slf4j
public class GatewayClientConfig {

    @Resource(name="printerDeliCloudAppClient")
    DeliCloudAppClient printerClient;

    @Bean("printerDeliCloudGatewayAppClient")
    public DeliGatewayApiClient getPrinterDeliGatewayApiClient(){
        return new DeliGatewayApiClient(printerClient);
    }
}