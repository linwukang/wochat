package com.lwk.wochat.api.data.redis;

import com.lwk.wochat.api.data.redis.value.RedisHashValue;
import com.lwk.wochat.api.data.redis.value.RedisListValue;

import java.time.Duration;
import java.util.Deque;
import java.util.Map;
import java.util.function.Supplier;

public interface RedisMap<K, V> extends Map<K, V> {
    /**
     * 逻辑移除 key
     * @param key 键
     * @return 移除的值
     */
    V markRemoved(K key);

    /**
     * 判断 key 是否被逻辑移除
     * @param key 键
     * @return
     * <ul>
     *     <li>{@code true}: 已逻辑移除</li>
     *     <li>{@code false}: 未逻辑移除</li>
     * </ul>
     */
    boolean isMarkRemoved(K key);

    /**
     * 设置 key 的值
     * @param key 键
     * @param value 值
     * @param ttl 过期时间
     * @return 值
     */
    V putWithExpire(K key, V value, Duration ttl);

    V putWithExpire(K key, V value, Supplier<Duration> ttl);

    void putAllWithExpire(Map<? extends K, ? extends V> m, Duration ttl);

    void putAllWithExpire(Map<? extends K, ? extends V> m, Supplier<Duration> ttl);

    /**
     * 设置 key 过期时间
     * @param key 键
     * @param timeout 过期时间
     * @return null when used in pipeline / transaction.
     */
    boolean expire(K key, Duration ttl);

    boolean expire(K key, Supplier<Duration> ttl);

    RedisListValue<K, V> list(K key);

    RedisHashValue<K, V> hash(K key);
}
