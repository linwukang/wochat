package com.lwk.wochat.account.service.impl;

import com.lwk.wochat.account.service.LoginTokenService;
import com.lwk.wochat.account.utils.StringUtil;
import com.lwk.wochat.api.dao.repository.AccountRepository;
import com.lwk.wochat.api.pojo.entity.Account;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Optional;

import static com.lwk.wochat.account.utils.StringConstant.*;

@Service
public class LoginTokenServiceImpl implements LoginTokenService {
    private final Logger logger = LoggerFactory.getLogger(LoginTokenServiceImpl.class);
//    private final RedisMap<String, String> stringRedisMap;

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private AccountRepository accountRepository;

//    public LoginTokenServiceImpl(@Autowired RedisMap<String, String> stringRedisMap) {
//        this.stringRedisMap = stringRedisMap.with(LOGIN_SERVICE_KEY_PREFIX);
//    }

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

//        stringRedisMap.put(TokenKeyPrefix + token, account.getId().toString());
//        stringRedisMap.put(UserIdTokenKeyPrefix + account.getId().toString(), token);

        redissonClient.getBucket(TOKEN_KEY_PREFIX + token).set(account.getId());
        redissonClient.getBucket(USER_ID_TOKEN_KEY_PREFIX + account.getId().toString()).set(token);

        logger.info("login: username=" + username + ", token=" + token);
        return token;
    }

    @Override
    public Optional<String> tryGetUsername(String token) {
//        return Optional.ofNullable(stringRedisMap.get(TOKEN_KEY_PREFIX + token));
        return Optional.ofNullable(
                (String) redissonClient
                        .getBucket(TOKEN_KEY_PREFIX + token)
                        .get());
    }

    @Override
    public Optional<Long> tryGetUserId(String token) {
        return tryGetUsername(token)
                .flatMap(username ->
                        accountRepository.findOne(Example
                                .of(Account
                                        .builder()
                                        .username(username)
                                        .build())))
                .map(account -> account.getId());
    }

    @Override
    public Optional<String> tryGetToken(String account) {
//        return Optional.ofNullable(stringRedisMap.get(USER_ID_TOKEN_KEY_PREFIX + account));
        return Optional.ofNullable((String) redissonClient
                .getBucket(USER_ID_TOKEN_KEY_PREFIX + account)
                .get());
    }

    @Override
    public void invalidateToken(String token) {
//        String userId = stringRedisMap.remove(TOKEN_KEY_PREFIX + token);
//        stringRedisMap.remove(USER_ID_TOKEN_KEY_PREFIX + userId);
        String userId = (String) redissonClient
                .getBucket(TOKEN_KEY_PREFIX + token)
                .get();
        redissonClient
                .getBucket(TOKEN_KEY_PREFIX + token)
                .delete();
        redissonClient
                .getBucket(USER_ID_TOKEN_KEY_PREFIX + userId)
                .delete();
    }

    @Override
    public void extendTokenValidityTo(String token, Duration ttl) {
//        stringRedisMap.expire(TOKEN_KEY_PREFIX + token, ttl);
        redissonClient
                .getBucket(TOKEN_KEY_PREFIX + token)
                .expire(ttl);
    }
}
