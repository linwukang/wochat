package com.lwk.wochat.redis_service.service;

import java.util.Optional;

/**
 * 对 redis 的基础操作
 */
public interface RedisService<T> {
    /**
     * 通过 key 获取值
     * @param key 键
     * @return 值
     */
    Optional<T> getByKey(String key, Class<T> type);

    /**
     * 通过 key 设置值，如果 key 不存在 则创建 key
     * @param key 键
     * @param value 值
     */
    void setByKey(String key, T value);

    /**
     * 通过 key 设置值，并指定存活时间，如果 key 不存在 则创建 key
     * @param key 键
     * @param value 值
     * @param ttl 存活时间
     */
    void setByKey(String key, T value, Long ttl);


    /**
     * 移除 key
     * @param key 键
     * @return
     * <ul>
     *     <li>{@code true}: 移除成功</li>
     *     <li>{@code false}: 移除失败，如 key 不存在等</li>
     * </ul>
     */
    Boolean removeKey(String key);
}
