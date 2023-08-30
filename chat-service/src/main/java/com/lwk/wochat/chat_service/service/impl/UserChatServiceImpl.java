package com.lwk.wochat.chat_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwk.wochat.api.dao.repository.UserChattingRecordRepository;
import com.lwk.wochat.api.data.redis.RedisMap;
import com.lwk.wochat.api.data.redis.value.RedisCounterValue;
import com.lwk.wochat.api.data.redis.value.RedisSortedSetValue;
import com.lwk.wochat.api.pojo.State;
import com.lwk.wochat.api.pojo.entity.UserChattingRecord;
import com.lwk.wochat.chat_service.service.UserChatService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserChatServiceImpl implements UserChatService {
    private final RabbitTemplate rabbitTemplate;
    private final Map<Long, WebSocketSession> userSocketSessionMap;
    private final RedisMap<String, String> stringRedisMap;
    private final RedisCounterValue<String, String> redisRecordIdCounter;
    private final UserChattingRecordRepository userChattingRecordRepository;

    public UserChatServiceImpl(
            @Autowired RabbitTemplate rabbitTemplate,
            @Autowired Map<Long, WebSocketSession> userSocketSessionMap,
            @Autowired RedisMap<String, String> stringRedisMap, UserChattingRecordRepository userChattingRecordRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.userSocketSessionMap = userSocketSessionMap;
        this.stringRedisMap = stringRedisMap.with("userChat");
        this.redisRecordIdCounter = this.stringRedisMap.with("records").counterWith("id");
        this.userChattingRecordRepository = userChattingRecordRepository;
    }

    @Override
    public Optional<Long> sendMessage(long senderId, long receiverId, String message) {
        WebSocketSession receiverSession = userSocketSessionMap.get(receiverId);
        try {
            long recordId = redisRecordIdCounter.increment();

            // userChat:records:<senderId>:<receiverId>
            RedisSortedSetValue<String, String> redisRecords = stringRedisMap
                    .with("records")
                    .with(String.valueOf(senderId))
                    .sortedSetWith(String.valueOf(receiverId), s -> (double) new Date().getTime());
            // 将消息存入 Redis 中
            redisRecords.add(message);

            UserChattingRecord chattingRecord = new UserChattingRecord();
            chattingRecord.setId(recordId);
            chattingRecord.setSenderId(senderId);
            chattingRecord.setReceiverId(receiverId);
            chattingRecord.setMessage(message);
            chattingRecord.setSendTime(new Date());
            chattingRecord.setState(State.NORMAL);


            // 通过接收者的 Session 将消息发送到接收者
            if (receiverSession != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                String messageJsonText = objectMapper.writeValueAsString(chattingRecord);

                receiverSession.sendMessage(new TextMessage(messageJsonText));
            }
            // 将消息异步的保存到数据库
//            userChattingRecordRepository.saveAsync(chattingRecord);
            return Optional.of(recordId);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<UserChattingRecord> getUserChattingRecords(long userId, long otherId, Date startTime, Date endTime) {
        return null;
    }

    @Override
    public boolean undoChattingRecord(long operatorId, long chattingRecordId) {
        return false;
    }

    @Override
    public Long getSenderId(long chattingRecordId) {
        return null;
    }
}
