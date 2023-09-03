package com.lwk.wochat.account.service.impl;

import com.lwk.wochat.account.service.VerificationCodeService;
import com.lwk.wochat.account.utils.StringUtil;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Objects;

import static com.lwk.wochat.account.utils.StringConstant.VERIFICATION_CODE_SERVICE_KEY_PREFIX;

public abstract class AbstractVerificationCodeService implements VerificationCodeService {
    protected final Logger logger;
//    protected final RedisMap<String, String> stringRedisMap;

    @Resource
    private RedissonClient redissonClient;

    public AbstractVerificationCodeService(/*@Autowired RedisMap<String, String> stringRedisMap*/) {
//        this.stringRedisMap = stringRedisMap.with("verificationCode");
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public String generateVerificationCode(int length, String content, Duration ttl) {
        String code = StringUtil.generateVerificationCode(length);
        /*if (!stringRedisMap.containsKey(code)) {
            stringRedisMap.put(code, content, ttl);
            return code;
        }*/
        if (redissonClient.getKeys().countExists(VERIFICATION_CODE_SERVICE_KEY_PREFIX + code) == 0) {
            redissonClient.getBucket(VERIFICATION_CODE_SERVICE_KEY_PREFIX + code).set(content);
            redissonClient.getBucket(VERIFICATION_CODE_SERVICE_KEY_PREFIX + code).expire(ttl);
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
//        String c = stringRedisMap.get(code);
        String c = (String) redissonClient.getBucket(VERIFICATION_CODE_SERVICE_KEY_PREFIX + code).get();

        return Objects.equals(c, content);
    }

    @Override
    public void invalidateVerificationCode(String code) {
//        stringRedisMap.remove(code);
        redissonClient.getBucket(VERIFICATION_CODE_SERVICE_KEY_PREFIX + code).delete();
    }
}
