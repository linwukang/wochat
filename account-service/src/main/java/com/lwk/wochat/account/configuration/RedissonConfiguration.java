package com.lwk.wochat.account.configuration;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfiguration {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Value("${spring.redis.database}")
    private int database;

    @Value("${spring.redis.username}")
    private String username;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setDatabase(database);
        if (username != null && username.length() > 0) {
            config.useSingleServer().setUsername(username);
        }
        if (password != null && password.length() > 0) {
            config.useSingleServer().setPassword(password);
        }

        return Redisson.create(config);
    }
}
