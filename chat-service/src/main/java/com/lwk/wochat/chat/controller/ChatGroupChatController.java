package com.lwk.wochat.chat.controller;

import com.lwk.wochat.chat.service.ChatGroupChatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import javax.annotation.Resource;

@RestController
@RequestMapping("/chat-group-chat")
public class ChatGroupChatController {
//    @Resource
    private ChatGroupChatService chatGroupChatService;
}
