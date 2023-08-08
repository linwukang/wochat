package com.lwk.wochat.chat_service.controller;

import com.lwk.wochat.chat_service.service.ChatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Resource
    private ChatService chatService;
}
