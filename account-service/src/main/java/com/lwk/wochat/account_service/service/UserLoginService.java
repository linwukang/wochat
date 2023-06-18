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
     * @param account Account 对象，包含用户名和密码
     * @return 如果登录成功则返回 token，否则返回 null
     */
    Optional<String> tryLogin(Account account);

    /**
     * 账号登出
     * @param account Account 对象，包含用户名
     * @param token account 对应的 token
     * @return
     *      - true: 登出成功
     *      - false: 登出失败
     */
    boolean logout(Account account, String token);

    /**
     * 检测账号是否已登录
     *
     * @param account Account 对象，包含用户名
     * @return - true: 已登录
     * - false: 未登录
     */
    boolean logged(Account account);

    /**
     * 检测账号是否存在
     * @param account 账号
     * @return
     * - true: 存在
     * - false: 不存在
     */
    boolean accountExisted(Account account);

    /**
     * 校验密码是否正确
     * @param account Account 对象，包含用户名和密码
     * @return
     * - true: 用户名密码正确
     * - false: 用户名或密码错误
     */
    boolean verifyPassword(Account account);
}
