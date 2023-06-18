package com.lwk.wochat.account_service.service.impl;

import com.lwk.wochat.account_service.service.VerificationCodeService;
import com.lwk.wochat.account_service.utils.StringUtil;
import com.lwk.wochat.api.data.redis.RedisMap;
import com.lwk.wochat.api.data.redis.RedisTemplateMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

public abstract class AbstractVerificationCodeService implements VerificationCodeService {
    protected final Logger logger;
    protected final RedisMap<String, String> redis;

    public AbstractVerificationCodeService(@Autowired RedisTemplate<String, String> redisTemplate) {
        redis = new RedisTemplateMap<>(redisTemplate, "verificationCodeService:");
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public String generateVerificationCode(int length, Duration ttl) {
        String code = StringUtil.generateVerificationCode(length);
        if (!checkVerificationCode(code)) {
            redis.putWithExpire(code, "", ttl);
            return code;
        }
        else {
            // code 已存在
            logger.error("验证码重复: code=" + code);
            throw new RuntimeException("验证码重复: code=" + code);
        }
    }

    @Override
    public boolean checkVerificationCode(String code) {
        return redis.containsKey(code);
    }

    @Override
    public void invalidateVerificationCode(String code) {
        redis.remove(code);
    }
}
