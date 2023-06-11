package com.lwk.wochat.api.clients;

import com.lwk.wochat.api.pojo.http.response.Result;
import com.lwk.wochat.api.utils.BeanUtil;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 来自 {@link com.lwk.wochat.redis_service.controller.RedisController}
 */
@Deprecated
@FeignClient(value = "redis-service", path = "redis")
public interface RedisClient {
    @GetMapping("/{key}")
    Result<String> get(@PathVariable String key);

    @PostMapping("/{key}")
    Result<String> save(@PathVariable String key, @RequestBody String value);

    @PostMapping("/{key}/{ttl}")
    Result<String> save(@PathVariable String key, @RequestBody String value, @PathVariable Long ttl);

    @DeleteMapping("/{key}")
    Result<String> remove(@PathVariable String key);


    default <T> Result<T> get(String key, Class<T> type) {
        Result<String> stringResult = get(key);
        return stringResult
                .getData()
                .map(s -> Result.getSucceed(BeanUtil.jsonStringToBean(s, type)))
                .orElse(Result.<T>getFailed());
    }

    default <T> Result<String> save(String key, T value) {
        return save(key, BeanUtil.beanToJsonString(value));
    }

    default <T> Result<String> save(String key, T value, Long ttl) {
        return save(key, BeanUtil.beanToJsonString(value), ttl);
    }
}
