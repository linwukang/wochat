package com.lwk.wochat.api.data.redis.value;

import java.util.Deque;

public interface RedisListValue<K, V> extends Deque<V>, RedisValue<K, V> {
    V get(int index);
    V set(int index, V element);
}
