package com.lwk.wochat.account_service.service.impl;

import com.lwk.wochat.account_service.service.LoginTokenService;
import com.lwk.wochat.account_service.utils.StringUtil;
import com.lwk.wochat.api.dao.repository.AccountRepository;
import com.lwk.wochat.api.data.redis.RedisMap;
import com.lwk.wochat.api.pojo.entity.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Optional;

import static com.lwk.wochat.account_service.utils.StringConstant.UserIdTokenKeyPrefix;
import static com.lwk.wochat.account_service.utils.StringConstant.TokenKeyPrefix;

@Service
public class LoginTokenServiceImpl implements LoginTokenService {
    private final Logger logger = LoggerFactory.getLogger(LoginTokenServiceImpl.class);
    private final RedisMap<String, String> stringRedisMap;

    @Resource
    private AccountRepository accountRepository;

    public LoginTokenServiceImpl(@Autowired RedisMap<String, String> stringRedisMap) {
        this.stringRedisMap = stringRedisMap;
    }

    @Override
    public String login(String username, String password) {
        String token = StringUtil.generateToken();
        Account account = accountRepository.findOne(
                Example.of(
                        Account
                                .builder()
                                .username(username)
                                .password(password)
                                .build()
                )
        ).get();

        stringRedisMap.put(TokenKeyPrefix + token, account.getId().toString());
        stringRedisMap.put(UserIdTokenKeyPrefix + account.getId().toString(), token);

        logger.info("login: username=" + username + ", token=" + token);
        return token;
    }

    @Override
    public Optional<String> tryGetUsername(String token) {
        return Optional.ofNullable(stringRedisMap.get(TokenKeyPrefix + token));
    }

    @Override
    public Optional<Long> tryGetUserId(String token) {
        return tryGetUsername(token)
                .map(username -> accountRepository.findOne(Example.of(Account.builder().username(username).build())).orElse(null))
                .map(account -> account.getId());
    }

    @Override
    public Optional<String> tryGetToken(String account) {
        return Optional.ofNullable(stringRedisMap.get(UserIdTokenKeyPrefix + account));
    }

    @Override
    public void invalidateToken(String token) {
        String userId = stringRedisMap.remove(TokenKeyPrefix + token);
        stringRedisMap.remove(UserIdTokenKeyPrefix + userId);
    }

    @Override
    public void extendTokenValidityTo(String token, Duration ttl) {
        stringRedisMap.expire(TokenKeyPrefix + token, ttl);
    }
}
