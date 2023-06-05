package com.lwk.wochat.account_service.controller;

import com.lwk.wochat.api.clients.RedisClient;
import com.lwk.wochat.api.pojo.http.response.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/account-service")
public class HelloController {
    @Resource(type = RedisClient.class)
    RedisClient redisClient;

//    @GetMapping("/hello")
//    public String hello() {
//        return "Hello Account Service";
//    }

    @GetMapping("/{key}")
    public Result<Object> get(@PathVariable String key) {
        System.out.println("key: " + key);
        return redisClient.get(key);
    }
}
