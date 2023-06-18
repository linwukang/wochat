package com.lwk.wochat.api.data.redis.value.impl;

import com.lwk.wochat.api.data.redis.value.RedisHashValue;
import org.springframework.data.redis.core.HashOperations;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class RedisHashValueImpl<K, V> implements RedisHashValue<K, V> {
    private final K key;
    private final HashOperations<K, K, V> hashOperations;

    @SuppressWarnings("all")
    public RedisHashValueImpl(K key, HashOperations<K, K, V> hashOperations) {
        this.key = key;

        this.hashOperations = hashOperations;

    }
    @Override
    public int size() {
        return Math.toIntExact(hashOperations.size(key));
    }

    @Override
    public boolean isEmpty() {
        return hashOperations.size(key) == 0L;
    }

    @Override
    public boolean containsKey(Object key) {
        return hashOperations.hasKey(this.key, key);
    }

    @Override
    public boolean containsValue(Object value) {
        return hashOperations
                .values(this.key)
                .contains(value);
    }

    @Override
    public V get(Object key) {
        return hashOperations.get(this.key, key);
    }

    @Override
    public V put(K key, V value) {
        hashOperations.put(this.key, key, value);
        return value;
    }

    @Override
    public V remove(Object key) {
        V value = hashOperations.get(this.key, key);
        hashOperations.delete(this.key, key);
        return value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        hashOperations.getOperations().delete(this.key);
    }

    @Override
    public Set<K> keySet() {
        return hashOperations.keys(this.key);
    }

    @Override
    public Collection<V> values() {
        return hashOperations.values(this.key);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return hashOperations
                .entries(this.key)
                .entrySet();
    }
}
