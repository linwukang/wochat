package com.lwk.wochat.account.utils;

public class StringConstant {
    public static final String ACCOUNT_REDIS_KEY_PREFIX = "wochat:accountService:";

    public static final String LOGIN_SERVICE_KEY_PREFIX = ACCOUNT_REDIS_KEY_PREFIX + "loginToken:";
    public static final String VERIFICATION_CODE_SERVICE_KEY_PREFIX = ACCOUNT_REDIS_KEY_PREFIX + "verificationCode:";
    public static final String TOKEN_KEY_PREFIX = ACCOUNT_REDIS_KEY_PREFIX + LOGIN_SERVICE_KEY_PREFIX + "token:";
    public static final String USER_ID_TOKEN_KEY_PREFIX = ACCOUNT_REDIS_KEY_PREFIX + LOGIN_SERVICE_KEY_PREFIX + "userIdToken:";
}
