package com.lwk.wochat.chat_service.controller;

import com.lwk.wochat.chat_service.service.UserChatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user-chat")
public class UserChatController {
    @Resource
    private UserChatService userChatService;
}
