package com.lwk.wochat.account_service.service;

import com.lwk.wochat.api.pojo.entity.Account;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Future;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public interface TokenService {
    /**
     * 通过 token 获取账号信息
     * @param token token
     * @return
     * <ul>
     *      <li>{@code Optional.empty()}: 账号未登录</li>
     *      <li>{@code Optional.of(account)}: 账号信息</li>
     * </ul>
     */
    Optional<Account> tryGetAccount(String token);

    /**
     * 通过 account 获取 token，当
     * 该服务不能对外暴露
     * @param account 用户账号
     * @return
     * <ul>
     *      <li>{@code Optional.empty()}: 账号未登录或 token 失效</li>
     *      <li>{@code Optional.of(token)}: token</li>
     * </ul>
     */
    Optional<String> tryGetToken(Account account);

    /**
     * 使 token 失效
     * 使 token 对应的账号退出登录
     * @param token token
     */
    void invalidateToken(String token);

    /**
     * 延长 token 的有效时间
     * @param validityDate 新的有效时间，只能是未来的某个时间点
     */
    void extendTokenValidityTo(@Future Date validityDate);
}
