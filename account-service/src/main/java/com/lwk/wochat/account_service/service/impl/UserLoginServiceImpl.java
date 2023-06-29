package com.lwk.wochat.account_service.service.impl;

import com.lwk.wochat.account_service.service.LoginTokenService;
import com.lwk.wochat.account_service.service.UserLoginService;
import com.lwk.wochat.api.dao.repository.AccountRepository;
import com.lwk.wochat.api.pojo.entity.Account;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class UserLoginServiceImpl implements UserLoginService {
    @Resource
    private AccountRepository accountRepository;

    @Resource
    private LoginTokenService tokenService;

    @Override
    public Optional<String> tryLogin(Account account) {
        if (verifyPassword(account)) {
            // 登录成功
            String token = tokenService.login(account);
            return Optional.of(token);
        }
        else {
            // 登录失败
            return Optional.empty();
        }
    }

    @Override
    public boolean logout(Account account, String token) {
        return tokenService
                .tryGetToken(account.getUsername())
                .map(tk -> {
                    if (tk.equals(token)) {
                        tokenService.invalidateToken(tk);
                        return true;        // 登出成功
                    }
                    else {
                        return false;       // token 不一致
                    }
                })
                .orElse(false);     // 未登录
    }

    @Override
    public boolean logged(Account account) {
        return tokenService
                .tryGetToken(account.getUsername())
                .isPresent();
    }

    @Override
    public boolean accountExisted(Account account) {
        return accountRepository.count(Example.of(account)) == 1;
    }

    @Override
    public boolean verifyPassword(Account account) {
        return accountRepository.count(Example.of(account)) == 1;
    }
}
