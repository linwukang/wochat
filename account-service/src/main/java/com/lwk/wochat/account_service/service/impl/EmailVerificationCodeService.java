package com.lwk.wochat.account_service.service.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 电子邮箱验证服务
 */
@Component
public class EmailVerificationCodeService extends AbstractVerificationCodeService {
    public EmailVerificationCodeService(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public boolean sendVerificationCode(String code, Duration ttl, String recipient) {
        // 发送电子邮箱
        logger.info("发送电子邮箱: email=" + recipient + ", code=" + code + ", ttl=" + ttl);
        return true;
    }
}
