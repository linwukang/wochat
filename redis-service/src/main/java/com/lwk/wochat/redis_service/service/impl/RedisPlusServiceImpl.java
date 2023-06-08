package com.lwk.wochat.redis_service.service.impl;

import com.lwk.wochat.api.data.serialize.Serializer;
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
    private RedisTemplate<String, String> redisTemplateString;

    private final String MarkRemovedPrefix = StringConstant.MarkRemovedPrefix;

    private final Serializer<String, T> serializer = new Serializer<>() {
        @Override
        public String serialize(T entity) {
            return BeanUtil.beanToJsonString(entity);
        }

        @Override
        public T deserialize(String serialized, Class<T> type) {
            return BeanUtil.jsonStringToBean(serialized, type);
        }
    };

    @Override
    public Optional<String> getByKey(String key) {
        String valueString = redisTemplateString.opsForValue().get(key);
        return valueString == null
                ? Optional.empty()
                : Optional.of(valueString);
    }

    @Override
    public Optional<T> getByKey(String key, Class<T> type) {
        String valueString = redisTemplateString.opsForValue().get(key);
        return valueString == null
                ? Optional.empty()
                : Optional.ofNullable(BeanUtil.mapToBean(valueString, type));
    }

    @Override
    public void setByKey(String key, T value) {
        redisTemplateString.opsForValue().set(key, serializer.serialize(value));
    }

    @Override
    public Boolean existsKey(String key) {
        return redisTemplateString.hasKey(key);
    }

    @Override
    public void setByKey(String key, T value, Long ttl) {
        redisTemplateString.opsForValue().set(key, serializer.serialize(value));
        redisTemplateString.expire(key, ttl, TimeUnit.MILLISECONDS);
    }

    @Override
    public Boolean removeKey(String key) {
        return redisTemplateString.delete(key);
    }

    @Override
    public Boolean markRemoved(String key) {
        String valueString = redisTemplateString.opsForValue().get(key);

        if (valueString != null && removeKey(key)) {
            redisTemplateString.opsForValue().set(MarkRemovedPrefix + key, valueString);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public Boolean isMarkRemoved(String key) {
        return existsKey(MarkRemovedPrefix + key);
    }
}
