package com.lwk.wochat.account_service.service.impl;

import com.lwk.wochat.account_service.service.TokenService;
import com.lwk.wochat.account_service.service.UserLoginService;
import com.lwk.wochat.api.dao.repository.AccountRepository;
import com.lwk.wochat.api.pojo.entity.Account;
import org.springframework.data.domain.Example;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.UUID;


public class UserLoginServiceImpl implements UserLoginService {
    @Resource
    private AccountRepository accountRepository;

    @Resource
    private TokenService tokenService;

    @Override
    public Optional<String> tryLogin(Account account) {
        long count = accountRepository.count(Example.of(account));
        if (count == 1) {
            // 登录成功
            String token = UUID.randomUUID().toString();
            return Optional.of(token);
        }
        else
        {
            // 登录失败
            return Optional.empty();
        }
    }

    @Override
    public Boolean logout(Account account, String token) {
        return tokenService
                .tryGetToken(account)
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
    public Boolean logged(Account account) {
        return tokenService
                .tryGetToken(account)
                .isPresent();
    }
}
