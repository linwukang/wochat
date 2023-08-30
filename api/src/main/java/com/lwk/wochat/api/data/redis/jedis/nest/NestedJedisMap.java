package com.lwk.wochat.api.data.redis.jedis.nest;

import com.lwk.wochat.api.data.redis.jedis.JedisMap;

public class NestedJedisMap<K, V> extends JedisMap<K, V> {
    public NestedJedisMap(JedisMap<K, V> jedisMap, String appendKeyPrefix) {
        super(
                jedisMap.jedis(),
                jedisMap.getSeparator(),
                jedisMap.getKeyPrefix() + jedisMap.getSeparator() + appendKeyPrefix,
                jedisMap.keySerializer(),
                jedisMap.valueSerializer(),
                jedisMap.keyEncoding,
                jedisMap.valueEncoding);
    }

    @Override
    public String getKeyPrefix() {
        return super.getKeyPrefix();
    }
}
