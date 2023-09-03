package com.lwk.wochat.chat.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class HttpSessionConfiguration {

    /**
     * 一个用于保存用户会话的 Map
     * @return Key 为用户 id， Value 为用户 Session 的 HashMap
     */
    @Bean(name = "userSocketSessionMap")
    public Map<Long, Session> userSocketSessionMap() {
        return new HashMap<>();
    }
}
