package com.lwk.wochat.api.exception;

/**、
 * 移除 key 失败异常
 */
public class RemoveKeyFailedException extends RuntimeException {
    public RemoveKeyFailedException(Object key) {
        super("Failed to remove data: key=" + key);
    }

}
