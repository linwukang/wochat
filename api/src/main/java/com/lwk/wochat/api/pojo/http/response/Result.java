package com.lwk.wochat.api.pojo.http.response;

import java.io.Serializable;
import java.util.Optional;

/**
 * 响应结果
 * 用于统一响应数据的内容
 * @param data 数据内容
 * @param code 状态码
 * @param message 消息
 * @param <T> 数据类型，必须实现可序列化接口 {@link Serializable}
 */
public record Result<T>(Optional<T> data, Code code, Optional<String> message) {

    public static <T> Result<T> error() {
        return new Result<>(
                Optional.empty(),
                Code.ERROR,
                Optional.of("服务器繁忙，请稍后重试")
        );
    }

    public static <T> Result<T> getSucceed(T data) {
        if (data == null) {
            throw new NullPointerException("data is null");
        }

        return new Result<>(
                Optional.of(data),
                Code.GET_SUCCEED,
                Optional.empty()
        );
    }

    public static <T> Result<T> getFailed() {
        return new Result<>(
                Optional.empty(),
                Code.GET_FAILED,
                Optional.of("获取数据失败")
        );
    }


    public static <T> Result<T> saveSucceed() {
        return new Result<>(
                Optional.empty(),
                Code.SAVA_SUCCEED,
                Optional.empty()
        );
    }

    public static <T> Result<T> saveFailed() {
        return new Result<>(
                Optional.empty(),
                Code.SAVA_FAILED,
                Optional.of("保存数据失败")
        );
    }

    public static <T> Result<T> removeSucceed() {
        return new Result<>(
                Optional.empty(),
                Code.REMOVE_SUCCEED,
                Optional.empty()
        );
    }

    public static <T> Result<T> removeFailed() {
        return new Result<>(
                Optional.empty(),
                Code.REMOVE_FAILED,
                Optional.of("删除数据失败")
        );
    }
}
