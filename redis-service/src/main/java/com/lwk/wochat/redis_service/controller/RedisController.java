package com.lwk.wochat.redis_service.controller;

import com.lwk.wochat.api.pojo.http.response.Result;
import com.lwk.wochat.redis_service.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("redis")
public class RedisController {
    Logger logger = LoggerFactory.getLogger(RedisController.class);

    @Resource(type = RedisService.class)
    private RedisService<String> redisService;

    @GetMapping("/{key}")
    public Result<String> get(@PathVariable String key) {
        Result<String> stringResult = redisService
                .getByKey(key, String.class)
                .map(Result::getSucceed)
                .orElse(Result.getFailed());

//        logger.info("redis controller get: key=" + key + ", result=" + stringResult);

        return stringResult;
    }

    @PostMapping("/{key}/{ttl}")
    public Result<String> save(@PathVariable String key, @RequestBody String value, @PathVariable Long ttl) {

        if (ttl == null) {
            return save(key, value);
        }
        else {
            redisService.setByKey(key, value, ttl);
            return Result.saveSucceed();
        }
    }

    @PostMapping("/{key}")
    public Result<String> save(@PathVariable String key, @RequestBody String  value) {
        redisService.setByKey(key, value);
        return Result.saveSucceed();
    }

    @DeleteMapping("/{key}")
    public Result<String> remove(@PathVariable String key) {
        return redisService.removeKey(key)
                ? Result.removeSucceed()
                : Result.removeFailed();
    }
}
