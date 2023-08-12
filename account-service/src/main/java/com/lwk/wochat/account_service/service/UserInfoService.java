package com.lwk.wochat.account_service.service;

import java.util.Date;

/**
 * 获取用户消息的服务
 */
public interface UserInfoService {
    /**
     * 获取用户名
     * @param userId 用户 id
     * @return 用户名，如果 userId 不存在则返回 null
     */
    String getUserName(long userId);

    /**
     * 通过用户名获取用户 id
     * @param username 用户名
     * @return 用户 id，如果 username 不存在则返回 null
     */
    Long getUserId(String username);

    /**
     * 获取账号创建的时间
     * @param userId 用户 id
     * @return 账号创建时间，如果 userId 不存在或用户未填写则返回 null
     */
    Date getAccountCreateTime(long userId);

    /**
     * 获取电子邮箱
     * @param userId 用户名
     * @return 电子邮箱，如果 userId 不存在或用户未填写则返回 null
     */
    String getEmail(long userId);

    /**
     * 获取电话号码
     * @param userId 用户 id
     * @return 电话号码，如果 userId 不存在或用户未填写则返回 null
     */
    String getPhone(long userId);

    /**
     * 获取用户的性别
     * @param userId 用户 id
     * @return 性别，如果 userId 不存在或用户未填写则返回 null
     */
    String getGender(long userId);

    /**
     * 获取用户的生日
     * @param userId 用户 id
     * @return 生日，如果 userId 不存在或用户未填写则返回 null
     */
    Date getBirthday(long userId);

    /**
     * 获取用户头像的 url
     * @param userId 用户 id
     * @return 头像的 url，如果 userId 不存在或用户未填写则返回 null
     */
    String getAvatarUrl(long userId);
}
