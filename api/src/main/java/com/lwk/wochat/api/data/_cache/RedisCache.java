package com.lwk.wochat.api.data._cache;

import com.lwk.wochat.api.clients.RedisClient;
import com.lwk.wochat.api.data.crud.Crud;
import com.lwk.wochat.api.exception.KeyNotFoundException;
import com.lwk.wochat.api.pojo.http.response.Result;
import com.lwk.wochat.api.utils.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class RedisCache<V> implements Cache<String, V> {

    Logger logger = LoggerFactory.getLogger(RedisCache.class);

    private final String keyPrefix;
    private final Crud<String, V> crud;
    private final RedisClient redisClient;

    public RedisCache(String name, Crud<String,V> crud, RedisClient redisClient) {
        keyPrefix = name + ":";
        this.crud = crud;
        this.redisClient = redisClient;
    }


    @Override
    public V get(String key, Class<V> type) {
        String fullKey = getFullKey(key);

        Result<String> stringResult = redisClient.get(fullKey);
        return stringResult
                .getData()
                .map(value -> BeanUtil.jsonStringToBean(value, type))
                .or(() -> {
                    logger.info("redis cache miss: fullKey=" + fullKey + ", " + type);
                    return Optional
                            .ofNullable(crud.retrieve(key))
                            .map(value -> {
                                // 写入 redis 缓存
                                redisClient.save(fullKey, BeanUtil.beanToJsonString(value));
                                return value;
                            });
                })
                .orElseThrow(() -> new KeyNotFoundException(fullKey));
    }


    @Override
    public void set(String key, V value, Class<V> type) {
        String fullKey = getFullKey(key);

        crud.update(key, value);
        Result<String> removeResult = redisClient.remove(fullKey);
//        if (Code.REMOVE_FAILED == removeResult.getCode()) {
//            throw new SaveDataFailedException(fullKey, value);
//        }
    }

    @Override
    public void remove(String key) {
        String fullKey = getFullKey(key);

        crud.delete(key);
        redisClient.remove(fullKey);
    }

    @Override
    public Optional<V> tryGet(String key, Class<V> type) {
        try {
            return Optional.ofNullable(get(key, type));
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Boolean trySet(String key, V value, Class<V> type) {
        try {
            set(key, value, type);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }


    @Override
    public Boolean tryRemove(String key) {
        try {
            remove(key);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    private String getFullKey(String key) {
        return keyPrefix + key;
    }

    @Override
    public Crud<String, V> getCrud() {
        return crud;
    }

    public V deserialize(String value, Class<V> type) {
        return BeanUtil.jsonStringToBean(value, type);
    }

    public String serialize(V value) {
        return BeanUtil.beanToJsonString(value);
    }
}
