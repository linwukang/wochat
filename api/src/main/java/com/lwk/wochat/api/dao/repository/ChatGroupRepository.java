package com.lwk.wochat.api.dao.repository;

import com.lwk.wochat.api.pojo.entity.ChatGroup;
import com.lwk.wochat.api.pojo.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ChatGroupRepository extends JpaRepository<ChatGroup, Long> {
}
