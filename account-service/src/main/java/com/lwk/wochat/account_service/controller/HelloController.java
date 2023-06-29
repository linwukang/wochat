package com.lwk.wochat.account_service.controller;

import com.lwk.wochat.api.dao.repository.AccountRepository;
import com.lwk.wochat.api.dao.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/account-service")
public class HelloController {
    Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Resource
    AccountRepository accountRepository;
    @Resource
    UserInfoRepository userInfoRepository;
    @GetMapping("/hello")
    public String hello() {
        return "Hello Account Service";
    }
}
