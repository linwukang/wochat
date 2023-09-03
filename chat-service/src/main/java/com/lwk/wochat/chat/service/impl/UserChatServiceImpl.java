package com.lwk.wochat.chat.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwk.wochat.api.dao.repository.UserChattingRecordRepository;
import com.lwk.wochat.api.pojo.State;
import com.lwk.wochat.api.pojo.entity.UserChattingRecord;
import com.lwk.wochat.api.pojo.entity.vo.UserChattingRecordVo;
import com.lwk.wochat.chat.service.UserChatService;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static com.lwk.wochat.chat.utils.StringConstant.USER_CHATTING_RECORDS_PREFIX;
import static com.lwk.wochat.chat.utils.StringConstant.USER_CHATTING_RECORD_ID_PREFIX;

@Service
public class UserChatServiceImpl implements UserChatService {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource(name = "userSocketSessionMap")
    private Map<Long, Session> userSocketSessionMap;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private UserChattingRecordRepository userChattingRecordRepository;


    @Override
    public Optional<Long> sendMessage(long senderId, long receiverId, String message) {
        Session receiverSession = userSocketSessionMap.get(receiverId);
        try {
            long recordId = redissonClient.getAtomicLong(USER_CHATTING_RECORD_ID_PREFIX).getAndIncrement();

            UserChattingRecord chattingRecord = new UserChattingRecord();
            chattingRecord.setId(recordId);
            chattingRecord.setSenderId(senderId);
            chattingRecord.setReceiverId(receiverId);
            chattingRecord.setMessage(message);
            chattingRecord.setSendTime(new Date());
            chattingRecord.setState(State.NORMAL);

            UserChattingRecordVo userChattingRecordVo = new UserChattingRecordVo(chattingRecord);

            ObjectMapper objectMapper = new ObjectMapper();

            RScoredSortedSet<String> records = redissonClient.getScoredSortedSet(USER_CHATTING_RECORDS_PREFIX + senderId + ":" + receiverId);

            // 将消息存入 Redis 中
            records.add(userChattingRecordVo.getSendTime().getTime(), objectMapper.writeValueAsString(userChattingRecordVo));

            // 通过接收者的 Session 将消息发送到接收者
            if (receiverSession != null) {
                String messageJsonText = objectMapper.writeValueAsString(chattingRecord);

                receiverSession.getAsyncRemote().sendText(messageJsonText);
            }
            // 将消息的保存到数据库
            userChattingRecordRepository.save(chattingRecord);
            return Optional.of(recordId);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<UserChattingRecord> getUserChattingRecords(long userId, long otherId, Date startTime, Date endTime) {
        List<UserChattingRecord> chattingRecords1 = getSendChattingRecords(userId, otherId, startTime, endTime);
        List<UserChattingRecord> chattingRecords2 = getSendChattingRecords(otherId, userId, startTime, endTime);
        return Stream
                .concat(chattingRecords1.stream(), chattingRecords2.stream())
                .sorted(Comparator.comparing(UserChattingRecord::getSendTime))
                .toList();
    }

    public List<UserChattingRecord> getSendChattingRecords(long senderId, long receiverId, Date startTime, Date endTime) {
        RScoredSortedSet<String> records = redissonClient.getScoredSortedSet(USER_CHATTING_RECORDS_PREFIX + senderId + ":" + receiverId);

        Collection<String> recordsString = records.valueRange(
                startTime.getTime(), false,
                endTime.getTime(), true);

        ObjectMapper objectMapper = new ObjectMapper();

        return recordsString
                .stream()
                .map(s -> {
                    try {
                        return objectMapper.readValue(s, UserChattingRecordVo.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(vo -> {
                    UserChattingRecord chattingRecord = new UserChattingRecord();
                    chattingRecord.setId(vo.getId());
                    chattingRecord.setSenderId(senderId);
                    chattingRecord.setReceiverId(receiverId);
                    chattingRecord.setMessage(vo.getMessage());
                    chattingRecord.setState(vo.getState());
                    chattingRecord.setSendTime(vo.getSendTime());

                    return chattingRecord;
                })
                .toList();
    }

    @Override
    public boolean recallChattingRecord(long operatorId, long chattingRecordId) {
        return false;
    }

    @Override
    public Long getSenderId(long chattingRecordId) {
        return null;
    }
}
