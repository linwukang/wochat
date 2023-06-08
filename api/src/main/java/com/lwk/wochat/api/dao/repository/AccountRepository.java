package com.lwk.wochat.api.dao.repository;

import com.lwk.wochat.api.pojo.entity.Account;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Cacheable("AccountRepository")
public interface AccountRepository extends JpaRepository<Account, Long> {

}
