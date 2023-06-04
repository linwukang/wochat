package com.lwk.wochat.redisservice;

import com.lwk.wochat.redisservice.configuration.RedisConfiguration;
import com.lwk.wochat.redisservice.configuration.SpringCacheConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ RedisConfiguration.class, SpringCacheConfiguration.class })
public class RedisServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisServiceApplication.class, args);
    }

}
