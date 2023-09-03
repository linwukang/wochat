package com.lwk.wochat.account.configuration;

import com.lwk.wochat.api.data.redis.RedisMap;
import com.lwk.wochat.api.data.redis.jedis.JedisMap;
import com.lwk.wochat.api.data.serialization.serializer.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class RedisMapConfiguration {
    @Bean
    public RedisMap<String, String> stringRedisMap(Jedis jedis) {
        return new JedisMap<>(
                jedis,
                ":",
                "account",
                new StringSerializer(),
                new StringSerializer());
    }
}
