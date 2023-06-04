package com.lwk.wochat.account_service.service.impl;

import com.lwk.wochat.account_service.service.TokenService;
import com.lwk.wochat.api.pojo.entity.Account;

import java.util.Date;
import java.util.Optional;

public class TokenServiceImpl implements TokenService {


    @Override
    public Optional<Account> tryGetAccount(String token) {
        return Optional.empty();
    }

    @Override
    public Optional<String> tryGetToken(Account account) {
        return Optional.empty();
    }

    @Override
    public void invalidateToken(String token) {

    }

    @Override
    public void extendTokenValidityTo(Date validityDate) {

    }
}
