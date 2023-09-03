package com.lwk.wochat.account.service;

import java.util.List;

/**
 * 获取聊天群信息
 */
public interface ChatGroupInfoService {
    String getChatGroupName(long groupId);

    Long getChatGroupId(String groupName);

    Integer getMembersMaxNumber(long groupId);

    Integer getMembersNumber(long groupId);

    String getAvatarUrl(long groupId);

    String getIntroduction(long groupId);

    List<String> getTags(long groupId);

    List<Long> getMembers(long groupId);
}
