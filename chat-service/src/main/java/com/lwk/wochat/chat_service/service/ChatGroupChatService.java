package com.lwk.wochat.chat_service.service;

import com.lwk.wochat.api.pojo.entity.UserChattingRecord;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ChatGroupChatService {
    /**
     * 向群组发送消息
     * @param senderId 发送用户 id
     * @param receiverId 接收群组 id
     * @param message 消息内容
     * @return 发送消息成功返回消息 id
     */
    Optional<Long> sendMessageToGroup(long senderId, long receiverId, String message);

    /**
     * 获取指定时间范围内，群组的所有聊天记录
     * @param groupId 群组 id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 聊天记录列表
     */
    List<UserChattingRecord> getGroupChattingRecords(long groupId, Date startTime, Date endTime);

    /**
     * 撤回聊天记录
     * @param operatorId 撤回操作者的用户 id
     * @param chattingRecordId 聊天记录的 id
     * @return 撤回成功返回 true，失败返回 false
     */
    boolean undoChattingRecord(long operatorId, long chattingRecordId);

    /**
     * 通过聊天记录 id 获取对应的发送者的用户 id
     * @param chattingRecordId 聊天记录 id
     * @return 发送者的用户 id，如果 chattingRecordId 则返回 null
     */
    Long getSenderId(long chattingRecordId);
}
