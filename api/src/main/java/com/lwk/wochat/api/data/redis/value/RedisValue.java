package com.lwk.wochat.api.data.redis.value;

import com.lwk.wochat.api.data.redis.RedisMap;

public interface RedisValue<K, V> {
    RedisMap<K, V> redis();
}
