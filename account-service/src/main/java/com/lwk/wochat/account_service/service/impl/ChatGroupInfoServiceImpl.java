package com.lwk.wochat.account_service.service.impl;

import com.lwk.wochat.account_service.service.ChatGroupInfoService;
import com.lwk.wochat.api.dao.repository.ChatGroupRepository;
import com.lwk.wochat.api.pojo.entity.ChatGroup;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatGroupInfoServiceImpl implements ChatGroupInfoService {
    @Resource
    private ChatGroupRepository chatGroupRepository;

    @Override
    public String getChatGroupName(long groupId) {
        return chatGroupRepository
                .findById(groupId)
                .map(g -> g.getGroupName())
                .orElse(null);
    }

    @Override
    public Long getChatGroupId(String groupName) {
        Example<ChatGroup> example = Example.of(ChatGroup.builder().groupName(groupName).build());
        return chatGroupRepository
                .findOne(example)
                .map(g -> g.getId())
                .orElse(null);
    }

    @Override
    public Integer getMembersMaxNumber(long groupId) {
        return chatGroupRepository
                .findById(groupId)
                .map(g -> g.getMembersMaxNumber())
                .orElse(null);
    }

    @Override
    public Integer getMembersNumber(long groupId) {
        return chatGroupRepository
                .findById(groupId)
                .map(g -> g.getMembersNumber())
                .orElse(null);
    }

    @Override
    public String getAvatarUrl(long groupId) {
        return chatGroupRepository
                .findById(groupId)
                .map(g -> g.getAvatarUrl())
                .orElse(null);
    }

    @Override
    public String getIntroduction(long groupId) {
        return chatGroupRepository
                .findById(groupId)
                .map(g -> g.getIntroduction())
                .orElse(null);
    }

    @Override
    public List<String> getTags(long groupId) {
        return chatGroupRepository
                .findById(groupId)
                .map(g -> g.getTags())
                .orElse(null);
    }

    @Override
    public List<Long> getMembers(long groupId) {
        return chatGroupRepository
                .findById(groupId)
                .map(g -> g
                        .getMembers()
                        .stream()
                        .map(a -> a.getId())
                        .collect(Collectors.toList()))
                .orElse(null);
    }
}
