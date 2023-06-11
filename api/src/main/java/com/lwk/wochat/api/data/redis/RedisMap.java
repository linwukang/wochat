package com.lwk.wochat.api.data.redis;

import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RedisMap<V> implements IRedisMap<String, V> {
    private final RedisTemplate<String, V> redisTemplate;

    private final String keyPrefix;

    private final String markRemovedPrefix = "@__markRemoved__:";

    public RedisMap(RedisTemplate<String, V> redisTemplate, String keyPrefix) {
        this.redisTemplate = redisTemplate;
        this.keyPrefix = keyPrefix;
    }

    @Override
    public int size() {
        return Optional
                .ofNullable(redisTemplate.keys(keyPrefix + "*"))
                .map(Set::size)
                .orElse(0);
    }

    @Override
    public boolean isEmpty() {
        return Optional
                .ofNullable(redisTemplate.keys(keyPrefix + "*"))
                .map(Set::isEmpty)
                .orElse(true);
    }

    @Override
    public boolean containsKey(Object key) {
        return Optional
                .ofNullable(key)
                .map(k -> keyPrefix + k)
                .map(redisTemplate::hasKey)
                .orElse(true);

    }

    @Override
    public boolean containsValue(Object value) {
        return Optional
                .ofNullable(redisTemplate.keys(keyPrefix + "*"))
                .map(keys -> keys.stream().map(key -> redisTemplate.opsForValue().get(key)))
                .map(values -> values.anyMatch(v -> v.equals(value)))
                .orElse(false);
    }

    @Override
    public V get(Object key) {
        return Optional
                .ofNullable(key)
                .map(k -> keyPrefix + k)
                .map(fullKey -> redisTemplate.opsForValue().get(fullKey))
                .orElse(null);
    }

    @Override
    public V put(String key, V value) {
        redisTemplate.opsForValue().set(keyPrefix + key, value);
        return value;
    }

    @Override
    public V remove(Object key) {
        return Optional.ofNullable(key)
                .map(k -> keyPrefix + k)
                .map(fullKey -> {
                    V value = redisTemplate.opsForValue().get(fullKey);
                    redisTemplate.delete(fullKey);
                    return value;
                })
                .orElse(null);
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> m) {
        m.forEach(
                (key, value) ->
                        redisTemplate
                                .opsForValue()
                                .set(keyPrefix + key, value));
    }

    @Override
    public void clear() {
        Optional
                .ofNullable(redisTemplate.keys(keyPrefix + "*"))
                .ifPresent(keys -> {
                    Long deleteCount = redisTemplate.delete(keys);
                });
    }

    @Override
    public Set<String> keySet() {
        return Optional
                .ofNullable(redisTemplate.keys(keyPrefix + "*"))
                .map(fullKeys ->
                        fullKeys
                                .stream()
//                                .filter(fullKey -> fullKey.startsWith(keyPrefix))
                                .map(fullKey -> fullKey.substring(keyPrefix.length())))
                .map(keys -> keys.collect(Collectors.toSet()))
                .orElse(Set.of());
    }

    @Override
    public Collection<V> values() {
        return Optional
                .ofNullable(redisTemplate.keys(keyPrefix + "*"))
                .map(keys -> keys.stream().map(key -> redisTemplate.opsForValue().get(key)))
                .map(vStream -> vStream.collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }

    @Override
    public Set<Entry<String, V>> entrySet() {
        return Optional
                .ofNullable(redisTemplate.keys(keyPrefix + "*"))
                .map(keys ->
                        keys
                                .stream()
                                .map(key ->
                                        new AbstractMap.SimpleEntry<>
                                                (key, redisTemplate.opsForValue().get(key)))
                                .map(entry -> (Entry<String, V>) entry))
                .map(s -> s.collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }

    @Override
    public V markRemoved(String key) {
        V value = remove(key);
        put(keyPrefix + markRemovedPrefix + key, value);
        return value;
    }

    @Override
    public boolean isMarkRemoved(String key) {
        return containsKey(keyPrefix + markRemovedPrefix + key);
    }

    @Override
    public V putWithExpire(String key, V value, Duration ttl) {
        redisTemplate.opsForValue().set(keyPrefix + key, value);
        expire(keyPrefix + key, ttl);
        return value;
    }

    @Override
    public V putWithExpire(String key, V value, Supplier<Duration> ttl) {
        redisTemplate.opsForValue().set(keyPrefix + key, value);
        expire(keyPrefix + key, ttl);
        return value;
    }

    @Override
    public void putAllWithExpire(Map<? extends String, ? extends V> m, Duration ttl) {
        m.forEach(
                (key, value) -> {
                    redisTemplate.opsForValue().set(keyPrefix + key, value);
                    redisTemplate.expire(keyPrefix + key, ttl);
                });
    }

    @Override
    public void putAllWithExpire(Map<? extends String, ? extends V> m, Supplier<Duration> ttl) {
        m.forEach(
                (key, value) -> {
                    redisTemplate.opsForValue().set(keyPrefix + key, value);
                    redisTemplate.expire(keyPrefix + key, ttl.get());
                });
    }

    @Override
    public Boolean expire(String key, Duration ttl) {
        return redisTemplate.expire(keyPrefix + key, ttl);
    }

    @Override
    public Boolean expire(String key, Supplier<Duration> ttl) {
        return redisTemplate.expire(keyPrefix + key, ttl.get());
    }
}
