package com.delicloud.app.miniprint.server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.delicloud")
//@EnableJpaRepositories(basePackages={"com.delicloud.app.miniprint.server.repository"})
@EntityScan("com.delicloud.app.miniprint.core.entity")
public class MiniPrintServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniPrintServerApplication.class, args);
    }

}
