package com.lwk.wochat.api.data.cache;

import com.lwk.wochat.api.data.crud.Crud;

import java.util.Optional;

public interface Cache<K, V> {
    V get(K key, Class<V> type);
    void set(K key, V value, Class<V> type);
    void remove(K key);

    Optional<V> tryGet(K key, Class<V> type);

    Boolean trySet(K key, V value, Class<V> type);

    Boolean tryRemove(K key);

    Crud<K, V> getCrud();
}
