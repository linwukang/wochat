package com.lwk.wochat.account.service;

import com.lwk.wochat.api.pojo.entity.Account;
import com.lwk.wochat.api.pojo.entity.UserInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 账号注册服务
 */
@Transactional
public interface RegistrationService {
    /**
     * 检测账号是否存在
     * @param account 账号
     * @return
     * - true: 存在
     * - false: 不存在
     */
    boolean accountExisted(Account account);

    /**
     * 注册账号
     * @param account 注册账号，包含用户名和密码信息
     * @return 账号信息
     * <ul>
     *     <li>{@code Optional.empty()}: 注册失败</li>
     *     <li>{@code Optional.of(account)}: 账号信息</li>
     * </ul>
     */
    Optional<Account> tryRegister(Account account);

    /**
     * 注册账号
     * @param account 注册账号，包含用户名和密码信息
     * @param userInfo 用户信息
     * @return 账号信息
     * <ul>
     *     <li>{@code Optional.empty()}: 注册失败</li>
     *     <li>{@code Optional.of(account)}: 账号信息</li>
     * </ul>
     */
    Optional<Account> tryRegisterWithUserInfo(Account account, UserInfo userInfo);

    /**
     * 注销账号
     * @param account 账号，包含用户名和密码信息
     * @return
     * <ul>
     *     <li>{@code true}: 注销成功</li>
     *     <li>{@code false}: 注销失败</li>
     * </ul>
     */
    boolean deregister(Account account);

}
