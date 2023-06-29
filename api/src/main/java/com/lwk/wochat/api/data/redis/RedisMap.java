package com.lwk.wochat.api.data.redis;

import com.lwk.wochat.api.data.redis.value.RedisHashValue;
import com.lwk.wochat.api.data.redis.value.RedisListValue;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
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

    boolean expire(K key, Supplier<Duration> ttlSupplier);

    /**
     * 通过 key 获取 list 值操作对象
     * @param key 键
     * @return list value
     */
    RedisListValue<K, V> list(K key);

    /**
     * 通过 key 获取 hash 值操作对象
     * @param key 键
     * @return hash value
     */
    RedisHashValue<K, V> hash(K key);

    RedisSerializer<K> keySerializer();

    RedisSerializer<V> valueSerializer();

    String pairKeyPrefix = "__pair__:";

    @SuppressWarnings("all")
    default void putPair(K item1, V item2) {
        String fullKeyString1 = pairKeyPrefix + new String(keySerializer().serialize(item1));
        K fullKey1 = keySerializer().deserialize(fullKeyString1.getBytes());
        put(fullKey1, item2);

        if (!Objects.equals(item1, item2))
        {
            String fullKeyString2 = pairKeyPrefix + new String(valueSerializer().serialize(item2));
            K fullKey2 = keySerializer().deserialize(fullKeyString2.getBytes());
            put(fullKey2, valueSerializer().deserialize(keySerializer().serialize(item1)));
        }
    }

    @SuppressWarnings("all")
    default V getOther(K key) {
        String fullKeyString = pairKeyPrefix + new String(keySerializer().serialize(key));
        K fullKey = keySerializer().deserialize(fullKeyString.getBytes());
        return get(fullKey);
    }

    @SuppressWarnings("all")
    default V removePair(K key) {
        String fullKeyString1 = pairKeyPrefix + new String(keySerializer().serialize(key));
        K fullKey1 = keySerializer().deserialize(fullKeyString1.getBytes());
        V v = remove(fullKey1);

        String fullKeyString2 = pairKeyPrefix + new String(valueSerializer().serialize(v));
        K fullKey2 = keySerializer().deserialize(fullKeyString2.getBytes());
        remove(fullKey2);

        return v;
    }
}
