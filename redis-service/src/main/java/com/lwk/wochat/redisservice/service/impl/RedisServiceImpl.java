package com.lwk.wochat.redisservice.service.impl;

import com.lwk.wochat.redisservice.service.RedisService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Optional;

@Service
public class RedisServiceImpl implements RedisService {

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public Optional<Serializable> getByKey(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    @Override
    public void setByKey(String key, Serializable value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setByKey(String key, Serializable value, Long ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    @Override
    public Boolean removeKey(String key) {
        return redisTemplate.delete(key);
    }
}
