package com.lwk.wochat.chat_service.service;

import com.lwk.wochat.api.pojo.entity.Account;

import java.util.Date;
import java.util.List;

public interface ChatService {
    Date sendMessage(Account sender, Account receiver, String message);
}
