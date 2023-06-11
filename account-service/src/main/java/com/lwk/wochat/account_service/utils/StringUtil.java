package com.lwk.wochat.account_service.utils;

import java.util.UUID;

public class StringUtil {

    /**
     * 生成随机 token 字符串
     * @return token
     */
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public static boolean isBlank(String string) {
        return string == null
                || string.length() == 0
                || string.trim().length() == 0;
    }

    public static boolean isNotBlank(String string) {
        return string != null
                && string.length() != 0
                && string.trim().length() > 0;
    }
}
