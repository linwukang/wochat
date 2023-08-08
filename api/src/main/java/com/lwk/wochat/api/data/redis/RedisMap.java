package com.lwk.wochat.api.data.redis;

import com.lwk.wochat.api.data.redis.value.RedisHashValue;
import com.lwk.wochat.api.data.redis.value.RedisListValue;
import com.lwk.wochat.api.data.redis.value.RedisSetValue;
import com.lwk.wochat.api.data.redis.value.RedisSortedSetValue;
import com.lwk.wochat.api.data.serialization.Serializer;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

public interface RedisMap<K, V> extends Map<K, V> {
    /**
     * 获取 key 分隔符
     * @return key 的分隔符
     */
    K getSeparator();

    /**
     * 获取 key 前缀
     * @return key 的前缀
     */
    K getKeyPrefix();

    /**
     * 获取 key 的序列化器
     * @return key 的序列化方式
     */
    Serializer<K> keySerializer();

    /**
     * 获取 value 的序列化方式
     * @return value 的序列化方式
     */
    Serializer<V> valueSerializer();

    /**
     * 通过 key 设置 value，同时指定过期时间
     * @param key key
     * @param value value
     * @param ttl 过期时间
     * @return value
     */
    V put(K key, V value, Duration ttl);

    /**
     * 通过 key 指定过期时间
     * @param key key
     * @param ttl 过期时间
     * @return 仅当 key 不存在时返回 false
     */
    boolean expire(K key, Duration ttl);

    /**
     * 通过 key 指定过期时间
     * @param key key
     * @param ttl 过期时间
     * @return 仅当 key 不存在时返回 false
     */
    boolean expire(K key, long ttl);

    RedisListValue<K, V> listOf(K key);

    RedisHashValue<K, V> hashOf(K key);

    RedisSetValue<K, V> setOf(K key);

    RedisSortedSetValue<K, V> sortedSetOf(K key, Function<? super V, Double> scorer);

//    Pairs<K, V> pairsOf(K key);
}
