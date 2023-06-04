package com.lwk.wochat.account_service.service;

import com.lwk.wochat.api.pojo.entity.Account;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public interface UserLoginService {
    /**
     * 尝试登录账号
     * @param account Account 对象，包含账号和密码
     * @return 如果登录成功则返回 token，否则返回 null
     */
    Optional<String> tryLogin(Account account);

    /**
     * 账号登出
     * @param account Account 对象，包含账号和密码
     * @param token account 对应的 token
     * @return
     *      - true: 登出成功
     *      - false: 登出失败
     */
    Boolean logout(Account account, String token);

    /**
     * 检测账号是否已登录
     * @param account Account 对象，包含账号和密码
     * @return
     *      - true: 已登录
     *      - false: 未登录
     */
    Boolean logged(Account account);
}
