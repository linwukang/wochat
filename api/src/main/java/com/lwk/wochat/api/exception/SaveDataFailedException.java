package com.lwk.wochat.api.exception;

/**
 * 保存数据失败异常
 */
public class SaveDataFailedException extends RuntimeException{
    public SaveDataFailedException(Object key, Object value) {
        super("Failed to save data: key=" + key + ", value=" + value);
    }
}
