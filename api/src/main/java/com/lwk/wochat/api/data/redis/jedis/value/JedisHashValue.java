package com.lwk.wochat.api.data.redis.jedis.value;

import com.lwk.wochat.api.data.redis.RedisMap;
import com.lwk.wochat.api.data.redis.jedis.JedisMap;
import com.lwk.wochat.api.data.redis.value.RedisHashValue;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.stream.Collectors;

public class JedisHashValue<K, V> implements RedisHashValue<K, V> {
    private final JedisMap<K, V> redisMap;
    private final K key;
    private final String fullKeyString;
    private final Jedis jedis;

    public JedisHashValue(JedisMap<K, V> redisMap, K key) {
        this.redisMap = redisMap;
        this.key = key;

        fullKeyString = redisMap.fullKey(key);
        jedis = redisMap.jedis();
    }

    @Override
    public int size() {
        return jedis
                .hkeys(fullKeyString)
                .size();
    }

    @Override
    public boolean isEmpty() {
        return redisMap.get(key) == null
                || size() == 0;
    }

    @Override
    public boolean containsKey(Object field) {
        if (!redisMap.containsKey(key)) {
            return false;
        }


        return jedis.hget(
                fullKeyString,
                redisMap.keyToString((K) field)) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!redisMap.containsKey(key)) {
            return false;
        }

        return values().contains(value);
    }

    @Override
    public V get(Object key) {
        String valueString = jedis.hget(
                fullKeyString,
                redisMap.keyToString((K) key));

        return valueString == null
                ? null
                : redisMap.stringToValue(valueString);
    }

    @Override
    public V put(K key, V value) {
        jedis.hset(fullKeyString, redisMap.keyToString(key), redisMap.valueToString(value));
        return value;
    }

    @Override
    public V remove(Object key) {
        String valueString = jedis.hget(fullKeyString, redisMap.keyToString((K) key));
        if (valueString == null) {
            return null;
        }

        long result = jedis.hdel(fullKeyString, redisMap.keyToString((K) key));
        if (result == 0) {
            return null;
        }

        return redisMap.stringToValue(valueString);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        Map<String, String> map = new HashMap<>();
        m.forEach((key, value) -> map.put(
                redisMap.keyToString(key),
                redisMap.valueToString(value)));

        jedis.hset(fullKeyString, map);
    }

    @Override
    public void clear() {
        jedis.del(fullKeyString);
    }

    @Override
    public Set<K> keySet() {
        Set<String> hashKeys = jedis.hkeys(fullKeyString);
        if (hashKeys == null || hashKeys.isEmpty()) {
            return Set.of();
        }

        return hashKeys
                .stream()
                .map(redisMap::stringToKey)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
        List<String> hashValues = jedis.hvals(fullKeyString);
        if (hashValues == null || hashValues.isEmpty()) {
            return Set.of();
        }

        return hashValues
                .stream()
                .map(redisMap::stringToValue)
                .collect(Collectors.toList());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return jedis
                .hgetAll(fullKeyString)
                .entrySet()
                .stream()
                .map(entry -> new Entry<K, V>() {
                    @Override
                    public K getKey() {
                        return redisMap.stringToKey(entry.getKey());
                    }

                    @Override
                    public V getValue() {
                        return redisMap.stringToValue(entry.getValue());
                    }

                    @Override
                    public V setValue(V value) {
                        return put(getKey(), value);
                    }
                })
                .collect(Collectors.toSet());
    }

    @Override
    public RedisMap<K, V> redis() {
        return redisMap;
    }
}
