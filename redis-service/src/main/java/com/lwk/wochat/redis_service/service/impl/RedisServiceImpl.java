package com.lwk.wochat.redis_service.service.impl;

import com.lwk.wochat.redis_service.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Optional<Object> getByKey(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    @Override
    public void setByKey(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setByKey(String key, Object value, Long ttl) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, ttl, TimeUnit.MILLISECONDS);
    }

    @Override
    public Boolean removeKey(String key) {
        return redisTemplate.delete(key);
    }
}
