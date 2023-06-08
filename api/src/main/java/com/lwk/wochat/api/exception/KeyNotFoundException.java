package com.lwk.wochat.api.exception;

/**
 * 未发现 key 异常
 */
public class KeyNotFoundException extends RuntimeException {
    public KeyNotFoundException(Object key) {
        super("key not found: key=" + key);
    }

}
