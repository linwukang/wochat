package com.lwk.wochat.account_service.service.impl;

import com.lwk.wochat.account_service.service.VerificationCodeService;
import com.lwk.wochat.account_service.utils.StringUtil;
import com.lwk.wochat.api.data.redis.RedisMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.Objects;

public abstract class AbstractVerificationCodeService implements VerificationCodeService {
    protected final Logger logger;
    protected final RedisMap<String, String> stringRedisMap;

    public AbstractVerificationCodeService(@Autowired RedisMap<String, String> stringRedisMap) {
        this.stringRedisMap = stringRedisMap;
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public String generateVerificationCode(int length, String content, Duration ttl) {
        String code = StringUtil.generateVerificationCode(length);
        if (!stringRedisMap.containsKey(code)) {
            stringRedisMap.put(code, content, ttl);
            return code;
        }
        else {
            // code 已存在
            logger.error("验证码重复: code=" + code);
            throw new RuntimeException("验证码重复: code=" + code);
        }
    }

    @Override
    public boolean checkVerificationCode(String code, String content) {
        String c = stringRedisMap.get(code);

        return Objects.equals(c, content);
    }

    @Override
    public void invalidateVerificationCode(String code) {
        stringRedisMap.remove(code);
    }
}
