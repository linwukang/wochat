package com.lwk.wochat.account.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class JedisConfiguration {
    @Value("${spring.redis.host}")
    public String host;

    @Value("${spring.redis.port}")
    public int port;

    @Value("${spring.redis.database}")
    public int database;

    @Value("${spring.redis.username:}")
    public String username;

    @Value("${spring.redis.password:}")
    public String password;

    @Bean
    public Jedis jedis() {
        Jedis jedis = new Jedis(host, port);
        if (username != null
                && username.length() > 0
                && password != null
                && password.length() > 0) {
            jedis.auth(username, password);
        }
        return jedis;
    }
}
