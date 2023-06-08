package com.lwk.wochat.redis_service.service;

import java.util.Map;
import java.util.Optional;

public interface RedisPlusService<T> {
    /**
     * 通过 key 获取值
     * @param key 键
     * @return 值
     */
    Optional<String> getByKey(String key);

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
     * 判断 key 是否存在
     * @param key 键
     * @return
     * <ul>
     *     <li>{@code true}: key 存在</li>
     *     <li>{@code false}: key 不存在</li>
     * </ul>
     */
    Boolean existsKey(String key);

    /**
     * 移除 key
     * @param key 键
     * @return
     * <ul>
     *     <li>{@code true}: 移除成功</li>
     *     <li>{@code false}: 移除失败</li>
     * </ul>
     */
    Boolean removeKey(String key);


    /**
     * 逻辑移除 key
     * @param key 键
     * @return
     * <ul>
     *     <li>{@code true}: 逻辑移除成功</li>
     *     <li>{@code false}: 逻辑移除失败</li>
     * </ul>
     */
    Boolean markRemoved(String key);

    /**
     * 判断 key 是否被逻辑移除
     * @param key 键
     * @return
     * <ul>
     *     <li>{@code true}: 已逻辑移除</li>
     *     <li>{@code false}: 未逻辑移除</li>
     * </ul>
     */
    Boolean isMarkRemoved(String key);
}
