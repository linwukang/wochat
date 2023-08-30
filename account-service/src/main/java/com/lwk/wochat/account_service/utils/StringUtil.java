package com.lwk.wochat.account_service.utils;

import java.util.Random;
import java.util.UUID;

public class StringUtil {
    private static final String TOKEN_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 生成随机 token 字符串
     * @return token
     */
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public static String generateVerificationCode(int length) {
        return generateVerificationCode(length, TOKEN_CHARACTERS);
    }

    public static String generateVerificationCode(int length, String characters) {
        if (length == 0) {
            throw new IllegalArgumentException("length == 0");
        }
        StringBuilder codeBuilder = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char c = characters.charAt(index);
            codeBuilder.append(c);
        }

        return codeBuilder.toString();
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
