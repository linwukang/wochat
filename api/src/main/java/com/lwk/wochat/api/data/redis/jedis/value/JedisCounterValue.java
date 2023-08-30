package com.lwk.wochat.api.data.redis.jedis.value;

import com.lwk.wochat.api.data.redis.RedisMap;
import com.lwk.wochat.api.data.redis.jedis.JedisMap;
import com.lwk.wochat.api.data.redis.value.RedisCounterValue;
import redis.clients.jedis.Jedis;

public class JedisCounterValue<K, V> implements RedisCounterValue<K, V> {
    private final JedisMap<K, V> redisMap;
    private final K key;
    private final String fullKeyString;
    private final Jedis jedis;

    public JedisCounterValue(JedisMap<K, V> redisMap, K key) {
        this.redisMap = redisMap;
        this.key = key;

        fullKeyString = redisMap.fullKey(key);
        jedis = redisMap.jedis();
    }

    @Override
    public boolean isZero() {
        String value = jedis.get(fullKeyString);
        if (value == null) {
            return true;
        }
        return Long.valueOf(value).equals(0L);
    }

    @Override
    public void makeZero() {
        jedis.set(fullKeyString, "0");
    }

    @Override
    public long value() {
        String value = jedis.get(fullKeyString);
        if (value == null) {
            makeZero();
            return 0L;
        }

        return Long.parseLong(value);
    }

    @Override
    public long increment() {
        return jedis.incr(fullKeyString);
    }

    @Override
    public long increment(long delta) {
        return jedis.incrBy(fullKeyString, delta);
    }

    @Override
    public long decrement() {
        return jedis.decr(fullKeyString);
    }

    @Override
    public long decrement(long delta) {
        return jedis.decrBy(fullKeyString, delta);
    }

    @Override
    public RedisMap<K, V> redis() {
        return redisMap;
    }
}
