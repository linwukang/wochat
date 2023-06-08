package com.lwk.wochat.api.configuration;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;


@Configuration
public class RedisConfiguration {
    @Bean
    @Qualifier("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        // 创建 RedisTemplate 对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置连接工厂
        template.setConnectionFactory(connectionFactory);

        // 创建 key 的序列化方式
        var redisKeySerializer = RedisSerializer.string();
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

    @Bean
    public <V> RedisTemplateFactory<V> redisTemplateFactory(RedisConnectionFactory connectionFactory) {
        return type -> {
            // 创建 RedisTemplate 对象
            RedisTemplate<String, V> template = new RedisTemplate<>();
            // 设置连接工厂
            template.setConnectionFactory(connectionFactory);

            // 创建 key 的序列化方式
            var redisKeySerializer = RedisSerializer.string();
            // 创建 value 的 JSON 序列化方式
            RedisSerializer<V> redisValueSerializer = new Jackson2JsonRedisSerializer<>(type);

            // 设置 Key 的序列化
            template.setKeySerializer(redisKeySerializer);
            template.setHashKeySerializer(redisKeySerializer);
            // 设置 Value 的序列化
            template.setValueSerializer(redisValueSerializer);
            template.setHashValueSerializer(redisValueSerializer);

            template.afterPropertiesSet();
            // 返回
            return template;
        };
    }

    public interface RedisTemplateFactory<V> {
        RedisTemplate<String, V> create(Class<V> type);
    }
}
