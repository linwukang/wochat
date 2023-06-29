package com.lwk.wochat.account_service.service.impl;

import com.lwk.wochat.account_service.service.LoginTokenService;
import com.lwk.wochat.account_service.utils.StringUtil;
import com.lwk.wochat.api.data.redis.RedisMap;
import com.lwk.wochat.api.data.redis.RedisTemplateMap;
import com.lwk.wochat.api.pojo.entity.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

import static com.lwk.wochat.account_service.utils.StringConstant.AccountTokenKeyPrefix;
import static com.lwk.wochat.account_service.utils.StringConstant.TokenKeyPrefix;

@Service
public class LoginTokenServiceImpl implements LoginTokenService {
    private final Logger logger = LoggerFactory.getLogger(LoginTokenServiceImpl.class);
    private final RedisMap<String, String> redis;

    public LoginTokenServiceImpl(@Autowired RedisTemplate<String, String> redisTemplate) {
        redis = new RedisTemplateMap<>(redisTemplate, "tokenService:");
    }

    @Override
    public String login(Account account) {
        String token = StringUtil.generateToken();

        redis.put(TokenKeyPrefix + token, account.getUsername());
        redis.put(AccountTokenKeyPrefix + account.getUsername(), token);

        logger.info("login: username=" + account.getUsername() + ", token=" + token);
        return token;
    }

    @Override
    public Optional<String> tryGetAccount(String token) {
        return Optional.ofNullable(redis.get(TokenKeyPrefix + token));
    }

    @Override
    public Optional<String> tryGetToken(String account) {
        return Optional.ofNullable(redis.get(AccountTokenKeyPrefix + account));
    }

    @Override
    public void invalidateToken(String token) {
        String account = redis.remove(TokenKeyPrefix + token);
        redis.remove(AccountTokenKeyPrefix + account);
    }

    @Override
    public void extendTokenValidityTo(String token, Duration ttl) {
        redis.expire(TokenKeyPrefix + token, ttl);
    }
}
