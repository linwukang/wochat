package com.lwk.wochat.api.data.redis.value;

import java.util.Map;

public interface RedisHashValue<K, V> extends Map<K, V>, RedisValue<K, V> {
}
