package com.lwk.wochat.account_service.controller;

import com.lwk.wochat.api.clients.RedisClient;
import com.lwk.wochat.api.pojo.entity.Account;
import com.lwk.wochat.api.pojo.http.response.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/account-service")
public class HelloController {
    Logger logger = LoggerFactory.getLogger(HelloController.class);

//    @Resource(type = RedisClient.class)
//    RedisClient redisClient;

    @GetMapping("/hello")
    public String hello() {
        return "Hello Account Service";
    }

//    @GetMapping("/{key}")
//    public Result<String> get(@PathVariable String key) {
//        logger.info("key=" + key);
//        redisClient.save(key, "傻逼");
//        return redisClient.get(key);
//    }
}
