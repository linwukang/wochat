package com.lwk.wochat.account_service.service.impl;

import com.lwk.wochat.account_service.service.TokenService;
import com.lwk.wochat.account_service.utils.StringUtil;
import com.lwk.wochat.api.clients.RedisClient;
import com.lwk.wochat.api.pojo.entity.Account;
import com.lwk.wochat.api.pojo.http.response.Code;
import com.lwk.wochat.api.pojo.http.response.Result;
import com.lwk.wochat.api.utils.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

import static com.lwk.wochat.account_service.utils.StringConstant.AccountTokenKeyPrefix;
import static com.lwk.wochat.account_service.utils.StringConstant.TokenKeyPrefix;

@Service
public class TokenServiceImpl implements TokenService {
    Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);
    @Resource
    RedisClient redisClient;

    @Override
    public String login(Account account) {
        String token = StringUtil.generateTokens();

        redisClient.save(TokenKeyPrefix + token, account);
        redisClient.save(AccountTokenKeyPrefix + account.getAccount(), token);

        logger.info("login: account=" + account + ", token=" + token);
        return token;
    }

    @Override
    public Optional<String> tryGetAccount(String token) {
        Result<String> result = redisClient.get(TokenKeyPrefix + token);
        if (Code.GET_SUCCEED.equals(result.getCode())) {
            return result.getData();
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> tryGetToken(String account) {
        Result<String> result = redisClient.get(AccountTokenKeyPrefix + account);
        if (Code.GET_SUCCEED.equals(result.getCode())) {
            return result.getData();
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void invalidateToken(String token) {
        Result<String> result = redisClient.remove(TokenKeyPrefix + token);

        if (Code.REMOVE_SUCCEED.equals(result.getCode())) {
            result
                    .getData()
                    .ifPresentOrElse(accountString -> {
                        Account account = BeanUtil.jsonStringToBean(accountString, Account.class);
                        Result<String> removeAccountLogin =
                                redisClient.remove(AccountTokenKeyPrefix + account.getAccount());
                        if (Code.REMOVE_SUCCEED.equals(removeAccountLogin.getCode())) {
                            logger.info("Removed token: token=" + token + ", account=" + account);
                        }
                        else {
                            logger.error("Failed to remove account token: token=" + token + ", account=" + account);
                            throw new RuntimeException("Failed to remove account token");
                        }
                    }, () -> {
                        logger.error("Failed to remove token: token=" + token);
                        throw new RuntimeException("Failed to remove token");
                    });

        } else {
            logger.warn("The attempt to remove the token failed: token=" + token);
        }
    }

    @Override
    public void extendTokenValidityTo(String token, Date validityDate) {
        Result<Account> result = redisClient.get(TokenKeyPrefix + token, Account.class);
        if (Code.GET_SUCCEED.equals(result.getCode())) {
            result
                    .getData()
                    .ifPresent(account -> {
                        long ttl = validityDate.getTime() - new Date().getTime();

                        if (ttl >= 0) {
                            redisClient.save(TokenKeyPrefix + token, account, ttl);
                        }
                        else {
                            // logging warning
                            logger.warn("TTL < 0: TTL=" + ttl);
                        }

                    });
        }
        else {
            // logging info
            logger.info("Token not found in Redis: token=" + token);
        }
    }
}
