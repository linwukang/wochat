package com.lwk.wochat.api.clients;

import com.lwk.wochat.api.pojo.http.response.Result;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "redis-service", path = "redis")
public interface RedisClient {
    @GetMapping("/{key}")
    Result<Object> get(@PathVariable String key);

    @PostMapping("/{key}")
    Result<Object> save(
            @PathVariable String key,
            @RequestBody Object value);

    @PostMapping("/{key}/{ttl}")
    Result<Object> save(
            @PathVariable String key,
            @RequestBody Object value,
            @PathVariable Long ttl);

    @DeleteMapping("/{key}")
    Result<Object> remove(@PathVariable String key);
}
