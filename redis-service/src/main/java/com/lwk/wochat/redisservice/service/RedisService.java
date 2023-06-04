package com.lwk.wochat.redisservice.service;

import java.io.Serializable;
import java.util.Optional;

/**
 * 对 redis 的基础操作
 */
public interface RedisService {
    /**
     * 通过 key 获取值
     * @param key 键
     * @return 值
     */
    Optional<Serializable> getByKey(String key);

    /**
     * 通过 key 设置值，如果 key 不存在 则创建 key
     * @param key 键
     * @param value 值
     */
    void setByKey(String key, Serializable value);

    /**
     * 通过 key 设置值，并指定存活时间，如果 key 不存在 则创建 key
     * @param key 键
     * @param value 值
     * @param ttl 存活时间
     */
    void setByKey(String key, Serializable value, Long ttl);

//    /**
//     * 通过 key 设置存活时间
//     * @param key 键
//     * @param ttl 存活时间
//     */
//    void setTtlByKey(String key, Long ttl);

    /**
     * 移除 key
     * @param key 键
     * @return
     * <ul>
     *     <li>{@code true}: 移除成功</li>
     *     <li>{@code false}: 移除成功</li>
     * </ul>
     */
    Boolean removeKey(String key);
}
