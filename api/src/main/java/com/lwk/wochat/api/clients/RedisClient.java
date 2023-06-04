package com.lwk.wochat.api.clients;

import com.lwk.wochat.api.pojo.http.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

@FeignClient("redis-service")
public interface RedisClient {
    @GetMapping("/{key}")
    Result<Serializable> get(@PathVariable String key);

    @PostMapping("/{key}/{ttl}")
    Result<Serializable> save(
            @PathVariable String key,
            @PathVariable(required = false) Long ttl,
            @RequestBody(required = false) Serializable value);

    @DeleteMapping("/{key}")
    Result<Serializable> remove(@PathVariable String key);
}
