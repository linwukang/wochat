package com.lwk.wochat.account_service.service.impl;

import com.lwk.wochat.api.data.redis.RedisMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 电子邮箱验证服务
 */
@Component
public class EmailVerificationCodeService extends AbstractVerificationCodeService {
    public EmailVerificationCodeService(@Autowired RedisMap<String, String> stringRedisMap) {
        super(stringRedisMap);
    }

    @Override
    public boolean sendVerificationCode(String code, Duration ttl, String recipient) {
        // 发送电子邮箱
        logger.info("发送电子邮箱: email=" + recipient + ", code=" + code + ", ttl=" + ttl);
        return true;
    }
}
