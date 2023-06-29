package com.lwk.wochat.account_service.service;

import java.time.Duration;

/**
 * 验证码服务
 */
public interface VerificationCodeService {
    /**
     * 生成指定长度的验证码
     * 在同一时刻不会出现相同的验证码
     * @param length 验证码长度
     * @param content 验证码检测内容
     * @param ttl 验证码有效时间
     * @return 验证码
     */
    String generateVerificationCode(int length, String content, Duration ttl);

    /**
     * 检测验证码
     * @param code 验证码
     * @param content 验证码检测内容
     * @return
     * - true: 验证通过
     * - false: 验证失败
     */
    boolean checkVerificationCode(String code, String content);

    /**
     * 使验证码失效
     * 验证码校验服务完成后应当使验证码失效
     * @param code 验证码
     */
    void invalidateVerificationCode(String code);

    /**
     * 发送验证码
     * @param code 验证码
     * @param ttl 验证码有效时间
     * @param recipient 验证码接收者，可以是电话、电子邮箱等
     * @return
     * - true: 发送成功
     * - false: 发送失败
     */
    boolean sendVerificationCode(String code, Duration ttl, String recipient);
}
