package com.lwk.wochat.redis_service.service.impl;

import com.lwk.wochat.api.utils.BeanUtil;
import com.lwk.wochat.redis_service.service.RedisPlusService;
import com.lwk.wochat.redis_service.utils.StringConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RedisPlusServiceImpl<T> implements RedisPlusService<T> {
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, String> redisTemplateString;

    @Override
    public Optional<String> getByKey(String key) {
        String valueMap = redisTemplateString.opsForValue().get(key);
        return valueMap == null
                ? Optional.empty()
                : Optional.of(valueMap);
    }

    @Override
    public Optional<T> getByKey(String key, Class<T> type) {
        Object valueMap = redisTemplate.opsForValue().get(key);
        return valueMap == null
                ? Optional.empty()
                : Optional.ofNullable(BeanUtil.mapToBean(valueMap, type));
    }

    @Override
    public void setByKey(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Boolean existsKey(String key) {
        return redisTemplate.hasKey(key);
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

    @Override
    public Boolean markRemoved(String key) {
        Object value = redisTemplate.opsForValue().get(key);

        if (value != null && removeKey(key)) {
            redisTemplate.opsForValue().set(StringConstant.MarkRemovedPrefix + key, value);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public Boolean isMarkRemoved(String key) {
        return existsKey(StringConstant.MarkRemovedPrefix + key);
    }
}
