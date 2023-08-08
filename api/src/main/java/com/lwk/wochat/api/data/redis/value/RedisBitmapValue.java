package com.lwk.wochat.api.data.redis.value;

import java.util.List;

public interface RedisBitmapValue<K, V> extends RedisValue<K, V> {
    boolean get(long index);

    List<Boolean> get(long start, long end);

    boolean set(long index, boolean value);

    boolean set(long index, boolean[] values);

    long count();

    long count(long start);

    long count(long start, long end);
}
