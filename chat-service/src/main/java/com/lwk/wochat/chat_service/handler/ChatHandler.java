package com.lwk.wochat.chat_service.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwk.wochat.chat_service.service.ChatService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;

@Component
@ServerEndpoint("/websocket/chat")
public class ChatHandler extends TextWebSocketHandler {
    public static final Map<Long, WebSocketSession> userSocketSessionMap;

    static {
        userSocketSessionMap = new HashMap<>();
    }

    @Resource
    private ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long userId = (Long) session.getAttributes().get("userId");

        userSocketSessionMap.putIfAbsent(userId, session);
    }

    /**
     * @param session 当前会话对象
     * @param message 文本消息，JSON 格式:
     *                {
     *                  'receiverId': Number,
     *                  'message': String,
     *                  'type': 'user' | 'group',
     *                }
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        long userId = (long) session.getAttributes().get("userId");

        JsonNode json = mapper.readTree(message.getPayload());
        long receiverId = json.get("receiverId").asLong();
        String msg = json.get("message").asText();
        String type = json.get("type").asText();

        if ("user".equals(type)) {
            // 向用户发送消息
            chatService.sendMessageToUser(userId, receiverId, msg);
        } else if ("group".equals(type)) {
            // 向群组发送消息
            chatService.sendMessageToGroup(userId, receiverId, msg);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, @Nonnull CloseStatus status) {
        Long userId = (Long) session.getAttributes().get("userId");
        userSocketSessionMap.remove(userId);
    }

}
