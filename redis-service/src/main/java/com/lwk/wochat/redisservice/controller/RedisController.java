package com.lwk.wochat.redisservice.controller;

import com.lwk.wochat.api.pojo.http.response.Result;
import com.lwk.wochat.redisservice.service.RedisService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;

@RestController
@RequestMapping("redis-service")
public class RedisController {
    @Resource(type = RedisService.class)
    private RedisService redisService;

    @GetMapping("/{key}")
    public Result<Serializable> get(@PathVariable String key) {
        return redisService
                .getByKey(key)
                .map(Result::getSucceed)
                .orElse(Result.getFailed());
    }

    @PostMapping("/{key}/{ttl}")
    public Result<Serializable> save(
            @PathVariable String key,
            @PathVariable(required = false) Long ttl,
            @RequestBody(required = false) Serializable value) {

        if (ttl == null) {
            redisService.setByKey(key, value);
        }
        else {
            redisService.setByKey(key, value, ttl);
        }
        return Result.saveSucceed();
    }

    @DeleteMapping("/{key}")
    public Result<Serializable> remove(@PathVariable String key) {
        return redisService.removeKey(key)
                ? Result.removeSucceed()
                : Result.removeFailed();
    }
}
