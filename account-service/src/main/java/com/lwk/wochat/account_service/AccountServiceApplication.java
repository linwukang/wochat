package com.lwk.wochat.account_service;

import com.lwk.wochat.account_service.configuration.NacosDiscoveryConfiguration;
import com.lwk.wochat.account_service.configuration.SpringCacheConfiguration;
import com.lwk.wochat.api.configuration.DataSourceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@Import({
        DataSourceConfiguration.class,
        NacosDiscoveryConfiguration.class,
        SpringCacheConfiguration.class
})
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

}
