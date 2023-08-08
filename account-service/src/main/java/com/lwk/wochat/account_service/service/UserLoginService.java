package com.lwk.wochat.account_service.service;

import com.lwk.wochat.api.pojo.entity.Account;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 用户登录服务
 * 基于 token 实现
 */
@Transactional
public interface UserLoginService {
    /**
     * 尝试登录账号
     * @param username 用户名
     * @param password 密码
     * @return 如果登录成功则返回 token，否则返回 null
     */
    Optional<String> tryLogin(String username, String password);

    /**
     * 账号登出
     * @param userId 用户 id
     * @param token 对应的 token
     * @return
     *      - true: 登出成功
     *      - false: 登出失败
     */
    boolean logout(long userId, String token);

    /**
     * 检测账号是否已登录
     *
     * @param userId 用户 id
     * @return
     *      - true: 已登录
     *      - false: 未登录
     */
    boolean logged(long userId);

    /**
     * 检测账号是否存在
     * @param username 用户名
     * @return
     * - true: 存在
     * - false: 不存在
     */
    boolean usernameExisted(String username);

    /**
     * 校验密码是否正确
     * @param username 用户名
     * @param password 密码
     * @return
     * - true: 用户名密码正确
     * - false: 用户名或密码错误
     */
    boolean verifyPassword(String username, String password);
}
