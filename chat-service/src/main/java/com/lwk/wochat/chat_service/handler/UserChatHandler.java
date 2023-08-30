package com.lwk.wochat.chat_service.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwk.wochat.chat_service.service.UserChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;

/**
 * 用户向用户发送消息
 */
@Component
@ServerEndpoint("/websocket/user-chat/{userId}")
public class UserChatHandler {
    private final static Logger logger = LoggerFactory.getLogger(UserChatHandler.class);
    // Spring 管理的都是单例（singleton）和 WebSocket （多对象）相冲突
    // 所以不能使用字段注入和构造注入
    private static Map<Long, Session> userSocketSessionMap;
    private static UserChatService userChatService;
    private Session session;
    private Long userId;

    @Autowired
    public void setUserSocketSessionMap(Map<Long, Session> userSocketSessionMap) {
        UserChatHandler.userSocketSessionMap = userSocketSessionMap;
    }

    @Autowired
    public void setUserChatService(UserChatService userChatService) {
        UserChatHandler.userChatService = userChatService;
    }

    @OnOpen
    public void afterConnectionEstablished(Session session, @PathParam(value="userId") Long userId) {
        userSocketSessionMap.putIfAbsent(userId, session);
        logger.info("已连接: userId=" + userId);

        this.session = session;
        this.userId = userId;
    }

    @OnClose
    public void afterConnectionClosed() {
        userSocketSessionMap.remove(userId);

        logger.info("已断开: userId=" + userId);
    }

    /**
     * @param message 文本消息，JSON 格式:
     *                {
     *                  'receiverId': Number,
     *                  'message': String,
     *                }
     */
    @OnMessage
    public void handleTextMessage(String message) throws JsonProcessingException {
        logger.info("message: " + message);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(message);

        long receiverId = json.get("receiverId").asLong();
        String msg = json.get("message").asText();

        // 向用户发送消息
        userChatService.sendMessage(userId, receiverId, msg);
    }

}
