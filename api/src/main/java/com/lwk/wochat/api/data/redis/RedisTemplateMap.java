package com.lwk.wochat.api.data.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RedisTemplateMap<K, V> implements RedisMap<K, V> {
    private final RedisTemplate<K, V> redisTemplate;

    private final K keyPrefix;

    private final K markRemovedPrefix;
    private final RedisSerializer<K> keySerializer;

    @SuppressWarnings("unchecked")
    public RedisTemplateMap(RedisTemplate<K, V> redisTemplate, String keyPrefix) {
        keySerializer = (RedisSerializer<K>) redisTemplate.getKeySerializer();
        this.redisTemplate = redisTemplate;
        this.keyPrefix = keySerializer.deserialize(keyPrefix.getBytes());
        markRemovedPrefix = keySerializer.deserialize("__markRemoved__:".getBytes());
    }

    private byte[] concatBytes(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    private K concat(K key, String s) {
        if (key instanceof String keyString) {
            return keySerializer.deserialize((keyString + s).getBytes());
        }

        byte[] keyBytes = Objects.requireNonNull(keySerializer.serialize(key));
        String keyString = new String(keyBytes);
        return keySerializer.deserialize((keyString + s).getBytes());
    }

    private K concat(String s, K key) {
        if (key instanceof String keyString) {
            return keySerializer.deserialize((s + keyString).getBytes());
        }

        byte[] keyBytes = Objects.requireNonNull(keySerializer.serialize(key));
        String keyString = new String(keyBytes);
        return keySerializer.deserialize((s + keyString).getBytes());
    }

    private K concat(K key1, K key2) {
        if (key1 instanceof String keyString1 && key2 instanceof String keyString2) {
            return keySerializer.deserialize((keyString1 + keyString2).getBytes());
        }

        byte[] keyBytes1 = Objects.requireNonNull(keySerializer.serialize(key1));
        String keyString1 = new String(keyBytes1);
        byte[] keyBytes2 = Objects.requireNonNull(keySerializer.serialize(key2));
        String keyString2 = new String(keyBytes2);
        return keySerializer.deserialize((keyString2 + keyString2).getBytes());
    }

    private K removeKeyPrefix(K key) {
        byte[] keySerialize = Objects.requireNonNull(keySerializer.serialize(key));
        byte[] keyPrefixSerialize = Objects.requireNonNull(keySerializer.serialize(keyPrefix));
        byte[] result = new byte[keySerialize.length - keyPrefixSerialize.length];

        System.arraycopy(keySerialize, keyPrefixSerialize.length, result, 0, result.length);

        return keySerializer.deserialize(result);
    }

    private K fullKey(K key) {
        return concat(keyPrefix, key);
    }

    @Override
    public int size() {
        return Optional
                .ofNullable(redisTemplate.keys(concat(keyPrefix, "*")))
                .map(Set::size)
                .orElse(0);
    }

    @Override
    public boolean isEmpty() {
        return Optional
                .ofNullable(redisTemplate.keys(concat(keyPrefix, "*")))
                .map(Set::isEmpty)
                .orElse(true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean containsKey(Object key) {
        return Optional
                .ofNullable(key)
                .map(k -> (K) k)
                .map(this::fullKey)
                .map(redisTemplate::hasKey)
                .orElse(true);
    }

    @Override
    public boolean containsValue(Object value) {
        return Optional
                .ofNullable(redisTemplate.keys(concat(keyPrefix, "*")))
                .map(keys -> keys.stream().map(key -> redisTemplate.opsForValue().get(key)))
                .map(values -> values.anyMatch(v -> v.equals(value)))
                .orElse(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        return Optional
                .ofNullable(key)
                .map(k -> (K) k)
                .map(this::fullKey)
                .map(fullKey -> redisTemplate.opsForValue().get(fullKey))
                .orElse(null);
    }

    @Override
    public V put(K key, V value) {
        redisTemplate.opsForValue().set(fullKey(key), value);
        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V remove(Object key) {
        return Optional.ofNullable(key)
                .map(k -> (K) k)
                .map(this::fullKey)
                .map(fullKey -> {
                    V value = redisTemplate.opsForValue().get(fullKey);
                    redisTemplate.delete(fullKey);
                    return value;
                })
                .orElse(null);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach(
                (key, value) ->
                        redisTemplate
                                .opsForValue()
                                .set(fullKey(key), value));
    }

    @Override
    public void clear() {
        Optional
                .ofNullable(redisTemplate.keys(concat(keyPrefix, "*")))
                .ifPresent(keys -> {
                    Long deleteCount = redisTemplate.delete(keys);
                });
    }

    @Override
    public Set<K> keySet() {
        return Optional
                .ofNullable(redisTemplate.keys(concat(keyPrefix, "*")))
                .map(fullKeys ->
                        fullKeys
                                .stream()
//                                .filter(fullKey -> fullKey.startsWith(keyPrefix))
                                .map(this::removeKeyPrefix))
                .map(keys -> keys.collect(Collectors.toSet()))
                .orElse(Set.of());
    }

    @Override
    public Collection<V> values() {
        return Optional
                .ofNullable(redisTemplate.keys(concat(keyPrefix, "*")))
                .map(keys -> keys.stream().map(key -> redisTemplate.opsForValue().get(key)))
                .map(vStream -> vStream.collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Optional
                .ofNullable(redisTemplate.keys(concat(keyPrefix, "*")))
                .map(keys ->
                        keys
                                .stream()
                                .map(key ->
                                        new AbstractMap.SimpleEntry<>
                                                (key, redisTemplate.opsForValue().get(key)))
                                .map(entry -> (Entry<K, V>) entry))
                .map(s -> s.collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }

    @Override
    public V markRemoved(K key) {
        V value = remove(key);
        put(concat(keyPrefix, concat(markRemovedPrefix, key)), value);
        return value;
    }

    @Override
    public boolean isMarkRemoved(K key) {
        return containsKey(concat(keyPrefix, concat(markRemovedPrefix, key)));
    }

    @Override
    public V putWithExpire(K key, V value, Duration ttl) {
        redisTemplate.opsForValue().set(fullKey(key), value);
        expire(concat(keyPrefix, key), ttl);
        return value;
    }

    @Override
    public V putWithExpire(K key, V value, Supplier<Duration> ttl) {
        redisTemplate.opsForValue().set(fullKey(key), value);
        expire(concat(keyPrefix, key), ttl);
        return value;
    }

    @Override
    public void putAllWithExpire(Map<? extends K, ? extends V> m, Duration ttl) {
        m.forEach(
                (key, value) -> {
                    redisTemplate.opsForValue().set(fullKey(key), value);
                    redisTemplate.expire(fullKey(key), ttl);
                });
    }

    @Override
    public void putAllWithExpire(Map<? extends K, ? extends V> m, Supplier<Duration> ttl) {
        m.forEach(
                (key, value) -> {
                    redisTemplate.opsForValue().set(fullKey(key), value);
                    redisTemplate.expire(fullKey(key), ttl.get());
                });
    }

    @Override
    public boolean expire(K key, Duration ttl) {
        return Boolean.TRUE.equals(redisTemplate.expire(fullKey(key), ttl));
    }

    @Override
    public boolean expire(K key, Supplier<Duration> ttl) {
        return Boolean.TRUE.equals(redisTemplate.expire(fullKey(key), ttl.get()));
    }
}
