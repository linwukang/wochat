package com.lwk.wochat.api.pojo.http.response;

import java.util.Objects;
import java.util.Optional;

/**
 * 响应结果
 * 用于统一响应数据的内容
 */
public final class Result<T> {
    private T data;
    private int code;
    private String message;

    /**
     */
    public Result(T data, int code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public Result(T data, int code) {
        this.data = data;
        this.code = code;
        this.message = null;
    }

    public Result(int code, String message) {
        this.data = null;
        this.code = code;
        this.message = null;
    }
    public Optional<T> getData() {
        return Optional.ofNullable(data);
    }

    public int getCode() {
        return code;
    }

    public Optional<String> getMessage() {
        return Optional.ofNullable(message);
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public static <T> Result<T> badRequest() {
        return new Result<>(
                null,
                Code.BAD_REQUEST,
                "服务器繁忙，请稍后重试"
        );
    }

    public static <T> Result<T> badRequest(String message) {
        return new Result<>(
                null,
                Code.BAD_REQUEST,
                message
        );
    }

    public static <T> Result<T> badRequest(T data, String message) {
        return new Result<>(
                data,
                Code.BAD_REQUEST,
                message
        );
    }


    public static <T> Result<T> ok() {
        return new Result<>(
                null,
                Code.OK,
                null
        );
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(
                data,
                Code.OK,
                null
        );
    }

    public static <T> Result<T> ok(T data, String message) {
        return new Result<>(
                data,
                Code.OK,
                message
        );
    }

    public static <T> Result<T> noContent() {
        return new Result<>(
                null,
                Code.NO_CONTENT
        );
    }

    public static <T> Result<T> noContent(String message) {
        return new Result<>(
                null,
                Code.NO_CONTENT,
                message
        );
    }

    public static <T> Result<T> unauthorized() {
        return new Result<>(
                null,
                Code.UNAUTHORIZED
        );
    }

    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(
                null,
                Code.UNAUTHORIZED,
                message
        );
    }

    public static <T> Result<T> notFound() {
        return new Result<>(
                null,
                Code.NOT_FOUND
        );
    }

    public static <T> Result<T> notFound(String message) {
        return new Result<>(
                null,
                Code.NOT_FOUND,
                message
        );
    }
}
