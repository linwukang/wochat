package com.lwk.wochat.chat_service.service;

import com.lwk.wochat.api.pojo.entity.Account;
import com.lwk.wochat.api.pojo.entity.ChattingRecord;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ChatService {
    /**
     * 向用户发送消息
     * @param senderId 发送用户 id
     * @param receiverId 接收用户 id
     * @param message 消息内容
     * @return 发送消息成功返回消息 id
     */
    Optional<Long> sendMessageToUser(long senderId, long receiverId, String message);

    /**
     * 向群组发送消息
     * @param senderId 发送用户 id
     * @param receiverId 接收群组 id
     * @param message 消息内容
     * @return 发送消息成功返回消息 id
     */
    Optional<Long> sendMessageToGroup(long senderId, long receiverId, String message);

    /**
     * 获取指定时间范围内，两个用户之间发送的所有聊天记录
     * @param userId 用户 id
     * @param otherId 另一个用户 id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 聊天记录列表
     */
    List<ChattingRecord> getUserChattingRecords(long userId, long otherId, Date startTime, Date endTime);

    /**
     * 获取指定时间范围内，群组的所有聊天记录
     * @param groupId 群组 id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 聊天记录列表
     */
    List<ChattingRecord> getGroupChattingRecords(long groupId, Date startTime, Date endTime);

}
