package com.lwk.wochat.api.data.redis.value;

import java.util.Set;

public interface RedisSetValue<K, V> extends Set<V>, RedisValue<K, V> {
}