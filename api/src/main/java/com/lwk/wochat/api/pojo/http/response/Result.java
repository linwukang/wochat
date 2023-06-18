package com.lwk.wochat.api.pojo.http.response;

import java.util.Objects;
import java.util.Optional;

/**
 * 响应结果
 * 用于统一响应数据的内容
 */
public final class Result<T> {
    private Optional<T> data;
    private Code code;
    private Optional<String> message;

    /**
     */
    public Result(Optional<T> data, Code code, Optional<String> message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public Result(T data, Code code, String message) {
        this.data = Optional.ofNullable(data);
        this.code = code;
        this.message = Optional.ofNullable(message);
    }

    public Result() {
        this(Optional.empty(), null, Optional.empty());
    }

    public Optional<T> getData() {
        return data;
    }

    public Code getCode() {
        return code;
    }

    public Optional<String> getMessage() {
        return message;
    }

    public void setData(T data) {
        this.data = Optional.ofNullable(data);
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = Optional.ofNullable(message);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;

        var that = (Result) obj;
        return Objects.equals(this.data, that.data) &&
                Objects.equals(this.code, that.code) &&
                Objects.equals(this.message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, code, message);
    }

    @Override
    public String toString() {
        return "Result[" +
                "data=" + data + ", " +
                "code=" + code + ", " +
                "message=" + message + ']';
    }


    /////////////////////////////////
    ///         静态方法           ///
    /////////////////////////////////

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
