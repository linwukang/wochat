package com.lwk.wochat.account.service.impl;

import com.lwk.wochat.account.service.RegistrationService;
import com.lwk.wochat.api.dao.repository.AccountRepository;
import com.lwk.wochat.api.dao.repository.UserInfoRepository;
import com.lwk.wochat.api.pojo.entity.Account;
import com.lwk.wochat.api.pojo.entity.UserInfo;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private UserInfoRepository userInfoRepository;

    @Override
    public boolean accountExisted(Account account) {
        return accountRepository.exists(Example.of(account));
    }

    @Override
    public Optional<Account> tryRegister(Account account) {
        Account accountUsername = Account
                .builder()
                .username(account.getUsername())
                .build();

        if (accountRepository.exists(Example.of(accountUsername))) {
            return Optional.empty();
        }
        else {
            return Optional.of(accountRepository.save(account));
        }
    }

    @Override
    public Optional<Account> tryRegisterWithUserInfo(Account account, UserInfo userInfo) {
        Optional<Account> accountOptional = tryRegister(account);

        if (accountOptional.isPresent()) {
            userInfo.setAccount(account);
            userInfoRepository.save(userInfo);
            return accountOptional;
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public boolean deregister(Account account) {
        if (!accountExisted(account)) {
            return false;
        }
        Optional<Account> oneAccount = accountRepository.findOne(Example.of(account));

        return oneAccount
                .map(acc -> {
                    accountRepository.deleteById(acc.getId());
                    return true;
                })
                .orElse(false);
    }
}
