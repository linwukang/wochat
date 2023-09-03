package com.lwk.wochat.chat.utils;

public class StringConstant {
    public static final String CHAT_REDIS_KEY_PREFIX = "wochat:chatService:";
    public static final String USER_CHAT_PREFIX = CHAT_REDIS_KEY_PREFIX + "user:";
    public static final String USER_CHATTING_RECORD_ID_PREFIX = USER_CHAT_PREFIX + "recordId";
    public static final String USER_CHATTING_RECORDS_PREFIX = USER_CHAT_PREFIX + "records:";
}
