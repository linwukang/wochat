package com.lwk.wochat.api;


import com.lwk.wochat.api.clients.RedisClient;
import com.lwk.wochat.api.clients.SbSpring;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaAuditing
public class ApiApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(ApiApplication.class, args);
//        SbSpring sbSpring = ac.getBean(SbSpring.class);
//        System.out.println("sbSpring: " + sbSpring);
//        RedisClient redisClient = ac.getBean(RedisClient.class);
//        System.out.println("redisClient: " + redisClient);
        RedisTemplate<String, Object> redisTemplate = ac.getBean("redisTemplate", RedisTemplate.class);
        System.out.println("redisTemplate: " + redisTemplate);
    }

}
