package com.lwk.wochat.api.data.adapter;

import org.springframework.cache.Cache;

import java.util.Map;
import java.util.concurrent.Callable;




/**
 * Map 到 Cache 的适配器类
 ** @noinspection NullableProblems*/
public class MapToCacheAdapter implements Cache {
    private final Map<Object, Object> map;
    public MapToCacheAdapter(Map<Object, Object> map) {
        this.map = map;
    }

    @Override
    public String getName() {
        return String.valueOf(map.hashCode());
    }

    @Override
    public Object getNativeCache() {
        return null;
    }

    @Override
    public ValueWrapper get(Object key) {
        return () -> map.get(key);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        else {
            return type.cast(value);
        }
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object value = map.get(key);
        if (value == null) {
            try {
                return valueLoader.call();
            } catch (Exception e) {
                return null;
            }
        }
        else {
            return (T) value;
        }
    }

    @Override
    public void put(Object key, Object value) {
        map.put(key, value);
    }

    @Override
    public void evict(Object key) {
        map.remove(key);
    }

    @Override
    public void clear() {
        map.clear();
    }
}
