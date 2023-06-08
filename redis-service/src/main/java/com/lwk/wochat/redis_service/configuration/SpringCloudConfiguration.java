package com.lwk.wochat.redis_service.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableFeignClients(basePackages = "com.lwk.wochat.api.clients")
public class SpringCloudConfiguration {
    @LoadBalanced       // 开启负载均衡
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
