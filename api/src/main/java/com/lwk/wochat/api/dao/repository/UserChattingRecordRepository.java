package com.lwk.wochat.api.dao.repository;

import com.lwk.wochat.api.data.jpa.repository.JpaRepositoryAsync;
import com.lwk.wochat.api.pojo.entity.UserChattingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserChattingRecordRepository extends JpaRepositoryAsync<UserChattingRecord, Long> {

}
