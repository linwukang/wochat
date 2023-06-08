package com.lwk.wochat.api.data.redis;

import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.stream.Collectors;

public class RedisMap<V> implements Map<String, V> {
    private final RedisTemplate<String, V> redisTemplate;

    private final String keyPrefix;

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
                .ifPresent(keys -> redisTemplate.delete(keys));
    }

    @Override
    public Set<String> keySet() {
        return Optional
                .ofNullable(redisTemplate.keys(keyPrefix + "*"))
                .map(fullKeys -> fullKeys.stream().map(fullKey -> fullKey.replace(keyPrefix, "")))
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
                                        new AbstractMap.SimpleEntry<>(key, redisTemplate.opsForValue().get(key)))
                                .map(entry -> (Entry<String, V>) entry))
                .map(s -> s.collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }
}
