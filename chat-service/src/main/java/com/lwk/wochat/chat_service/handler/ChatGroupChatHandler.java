package com.lwk.wochat.chat_service.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwk.wochat.chat_service.service.ChatGroupChatService;
import com.lwk.wochat.chat_service.service.UserChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户向聊天群发送消息
 */
@Component
@ServerEndpoint("/websocket/chat-group-chat")
public class ChatGroupChatHandler extends TextWebSocketHandler {
    private final static Logger logger = LoggerFactory.getLogger(UserChatHandler.class);
    // Spring 管理的都是单例（singleton）和 WebSocket （多对象）相冲突
    // 所以不能使用字段注入和构造注入
    private static Map<Long, Session> userSocketSessionMap;
    private static ChatGroupChatService groupChatService;
    private Session session;
    private Long userId;

    @Autowired
    public void setUserSocketSessionMap(Map<Long, Session> userSocketSessionMap) {
        ChatGroupChatHandler.userSocketSessionMap = userSocketSessionMap;
    }

    @Autowired
    public void setUserChatService(ChatGroupChatService groupChatService) {
        ChatGroupChatHandler.groupChatService = groupChatService;
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
        groupChatService.sendMessageToGroup(userId, receiverId, msg);
    }
}
