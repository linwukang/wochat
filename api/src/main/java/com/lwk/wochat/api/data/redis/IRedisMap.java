package com.lwk.wochat.api.data.redis;

import java.time.Duration;
import java.util.Map;

public interface IRedisMap<K, V> extends Map<K, V> {
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

    V putAndExpire(K key, V value, Duration timeout);

    Boolean expire(K key, Duration timeout);
}
