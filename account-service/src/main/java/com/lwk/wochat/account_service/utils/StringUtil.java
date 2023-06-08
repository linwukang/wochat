package com.lwk.wochat.account_service.utils;

import java.util.UUID;

public class StringUtil {

    /**
     * 生成随机 token 字符串
     * @return token
     */
    public static String generateTokens() {
        return UUID.randomUUID().toString();
    }
}
