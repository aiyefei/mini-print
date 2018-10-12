package com.delicloud.app.miniprint.server.config;

import com.delicloud.platform.cloudapp.gateway.sdk.DeliCloudAppClient;
import com.delicloud.platform.common.lang.util.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class CloudClientConfig {

    @Value("${delicloud.printer.appid}")
    private String printerAppId;
    @Value("${delicloud.printer.appkey}")
    private String printerAppKey;
    @Value("${delicloud.printer.endpoint:\"\"}")
    private String printerEndPoint;

    @Value("${delicloud.webhook.expire: 172800}")
    private int webhookExpireSeconds;


    @Bean("printerDeliCloudAppClient")
    public DeliCloudAppClient getPrinterDeliCloudAppClient() {
        DeliCloudAppClient client = new DeliCloudAppClient(printerAppId, printerAppKey);

        if (!MyStringUtils.isEmpty(printerEndPoint)) {
            client.setEndPoint(printerEndPoint);
        }

        client.setWebhookExpireSeconds(webhookExpireSeconds);
        return client;
    }

}