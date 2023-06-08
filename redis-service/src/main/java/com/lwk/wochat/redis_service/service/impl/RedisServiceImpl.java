package com.lwk.wochat.redis_service.service.impl;

import com.lwk.wochat.api.utils.BeanUtil;
import com.lwk.wochat.redis_service.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl<T> implements RedisService<T> {

    Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Optional<T> getByKey(String key, Class<T> type) {
        Object valueMap = redisTemplate.opsForValue().get(key);
        if (valueMap == null) {
            return Optional.empty();
        }
        T value = BeanUtil.mapToBean(valueMap, type);


//        logger.info("redis get: key=" + key + ", value=" + value);
        return Optional.ofNullable(value);
    }

    @Override
    public void setByKey(String key, T value) {
        redisTemplate.opsForValue().set(key, value);

//        logger.info("redis set: key=" + key + ", value=" + value);
    }

    @Override
    public void setByKey(String key, Object value, Long ttl) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, ttl, TimeUnit.MILLISECONDS);

//        logger.info("redis set: key=" + key + ", value=" + value + ", TTL=" + ttl);
    }

    @Override
    public Boolean removeKey(String key) {
//        logger.info("redis remove: key=" + key);

        return redisTemplate.delete(key);
    }
}
