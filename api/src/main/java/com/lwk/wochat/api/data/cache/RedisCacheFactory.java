package com.lwk.wochat.api.data.cache;

import com.lwk.wochat.api.clients.RedisClient;
import com.lwk.wochat.api.data.crud.Crud;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisCacheFactory<V> implements CacheFactory<String, V> {

    @Resource
    private RedisClient redisClient;

    @Override
    public Cache<String, V> create(Crud<String, V> crud) {
        return new RedisCache<>("redisCache" + this.hashCode(), crud, redisClient);
    }

    public Cache<String, V> create(String name, Crud<String, V> crud) {
        return new RedisCache<>(name, crud, redisClient);
    }
}
