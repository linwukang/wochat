package com.lwk.wochat.api.data.redis.jedis.value;

import com.lwk.wochat.api.data.redis.RedisMap;
import com.lwk.wochat.api.data.redis.jedis.JedisMap;
import com.lwk.wochat.api.data.redis.jedis.utils.LuaScripts;
import com.lwk.wochat.api.data.redis.value.RedisBitmapValue;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.stream.Collectors;

public class JedisBitmapValue<K, V> implements RedisBitmapValue<K, V> {
    private final JedisMap<K, V> redisMap;
    private final K key;
    private final String fullKeyString;
    private final Jedis jedis;

    public JedisBitmapValue(JedisMap<K, V> redisMap, K key) {
        this.redisMap = redisMap;
        this.key = key;

        fullKeyString = redisMap.fullKey(key);
        jedis = redisMap.jedis();
    }

    @Override
    public boolean get(long index) {
        return jedis.getbit(fullKeyString, index);
    }

    @Override
    public List<Boolean> get(long start, long end) {
        Object result = jedis.eval(
                LuaScripts.REDIS_GET_RANGE,
                List.of(fullKeyString),
                List.of(String.valueOf(start), String.valueOf(end)));

        if (result instanceof List) {
            return ((List<String>) result)
                    .stream()
                    .map("1"::equals)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public boolean set(long index, boolean value) {
        return false;
    }

    @Override
    public boolean set(long index, boolean[] values) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public long count(long start) {
        return 0;
    }

    @Override
    public long count(long start, long end) {
        return 0;
    }

    @Override
    public RedisMap<K, V> redis() {
        return null;
    }
}
