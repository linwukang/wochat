package com.lwk.wochat.redis_service.controller;

import com.lwk.wochat.api.pojo.http.response.Result;
import com.lwk.wochat.redis_service.service.RedisService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;

@RestController
@RequestMapping("redis")
public class RedisController {
    @Resource(type = RedisService.class)
    private RedisService redisService;

    @GetMapping("/{key}")
    public Result<Object> get(@PathVariable String key) {
        return redisService
                .getByKey(key)
                .map(Result::getSucceed)
                .orElse(Result.getFailed());
    }

    @PostMapping("/{key}")
    public Result<Object> save(
            @PathVariable String key,
            @RequestBody Object value) {

            redisService.setByKey(key, value);
        return Result.saveSucceed();
    }

    @PostMapping("/{key}/{ttl}")
    public Result<Object> save(
            @PathVariable String key,
            @RequestBody Object value,
            @PathVariable Long ttl) {

        if (ttl == null) {
            return Result.saveFailed();
        }
        else {
            redisService.setByKey(key, value, ttl);
            return Result.saveSucceed();
        }
    }

    @DeleteMapping("/{key}")
    public Result<Object> remove(@PathVariable String key) {
        return redisService.removeKey(key)
                ? Result.removeSucceed()
                : Result.removeFailed();
    }
}
