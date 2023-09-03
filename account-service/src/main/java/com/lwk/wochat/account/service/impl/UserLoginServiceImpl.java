package com.lwk.wochat.account.service.impl;

import com.lwk.wochat.account.service.LoginTokenService;
import com.lwk.wochat.account.service.UserLoginService;
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
    public Optional<String> tryLogin(String username, String password) {
        if (verifyPassword(username, password)) {
            // 登录成功
            String token = tokenService.login(username, password);
            return Optional.of(token);
        }
        else {
            // 登录失败
            return Optional.empty();
        }
    }

    @Override
    public boolean logout(long userId, String token) {
        Optional<Account> account = accountRepository.findById(userId);
        if (account.isEmpty()) {
            return false;
        }

        return tokenService
                .tryGetToken(account.get().getUsername())
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
    public boolean logged(long userId) {
        Optional<Account> account = accountRepository.findById(userId);
        if (account.isEmpty()) {
            return false;
        }

        return tokenService
                .tryGetToken(account.get().getUsername())
                .isPresent();
    }

    @Override
    public boolean usernameExisted(String username) {
        return accountRepository.count(
                Example.of(
                        Account
                                .builder()
                                .username(username)
                                .build())) == 1;
    }

    @Override
    public boolean verifyPassword(String username, String password) {
        return accountRepository.count(
                Example.of(
                        Account
                                .builder()
                                .username(username)
                                .password(password)
                                .build())) == 1;
    }
}
