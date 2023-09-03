package com.lwk.wochat.account.service.impl;

import com.lwk.wochat.account.service.UserInfoService;
import com.lwk.wochat.api.dao.repository.AccountRepository;
import com.lwk.wochat.api.pojo.entity.Account;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private AccountRepository accountRepository;

    @Override
    public String getUserName(long userId) {
        return accountRepository
                .findById(userId)
                .map(account -> account.getUsername())
                .orElse(null);
    }

    @Override
    public Long getUserId(String username) {
        Example<Account> example = Example.of(Account.builder().username(username).build());
        return accountRepository
                .findOne(example)
                .map(account -> account.getId())
                .orElse(null);
    }

    @Override
    public Date getAccountCreateTime(long userId) {
        return accountRepository
                .findById(userId)
                .map(Account::getCreateTime)
                .orElse(null);
    }

    @Override
    public String getEmail(long userId) {
        return accountRepository
                .findById(userId)
                .map(account -> account.getUserInfo().getEmail())
                .orElse(null);
    }

    @Override
    public String getPhone(long userId) {
        return accountRepository
                .findById(userId)
                .map(account -> account.getUserInfo().getPhone())
                .orElse(null);
    }

    @Override
    public String getGender(long userId) {
        return accountRepository
                .findById(userId)
                .map(account -> account.getUserInfo().getGender())
                .orElse(null);
    }

    @Override
    public Date getBirthday(long userId) {
        return accountRepository
                .findById(userId)
                .map(account -> account.getUserInfo().getBirthDate())
                .orElse(null);
    }

    @Override
    public String getAvatarUrl(long userId) {
        return accountRepository
                .findById(userId)
                .map(account -> account.getUserInfo().getAvatarUrl())
                .orElse(null);
    }
}
