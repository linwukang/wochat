package com.lwk.wochat.account_service;

import com.lwk.wochat.account_service.controller.HelloController;
import com.lwk.wochat.api.clients.RedisClient;
import com.lwk.wochat.api.clients.SbSpring;
import com.lwk.wochat.api.configuration.SpringCloudConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
//@Import(SpringCloudConfiguration.class)
public class AccountServiceApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(AccountServiceApplication.class, args);
//        HelloController helloController = ac.getBean(HelloController.class);
//        System.out.println("helloController: " + helloController);
//        SbSpring sbSpring = ac.getBean(SbSpring.class);
//        System.out.println("sbSpring: " + sbSpring);
//        RedisClient redisClient = ac.getBean(RedisClient.class);
//        System.out.println("redisClient: " + redisClient);
    }

}
