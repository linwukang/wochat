package com.lwk.wochat.redis_service.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;


@Configuration
public class RedisConfiguration {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        // 创建 RedisTemplate 对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置连接工厂
        template.setConnectionFactory(connectionFactory);

        // 创建 key 的前缀序列化方式
        var redisKeySerializer = new RedisKeyPrefixSerializer();
        // 创建 value 的 JSON 序列化方式
        RedisSerializer<Object> redisValueSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        // 设置 Key 的序列化
        template.setKeySerializer(redisKeySerializer);
        template.setHashKeySerializer(redisKeySerializer);
        // 设置 Value 的序列化
        template.setValueSerializer(redisValueSerializer);
        template.setHashValueSerializer(redisValueSerializer);
        // 返回
        return template;
    }
}
