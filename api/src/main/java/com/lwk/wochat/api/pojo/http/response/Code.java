package com.lwk.wochat.api.pojo.http.response;

public enum Code {
    GET_SUCCEED,        // 获取成功
    GET_FAILED,         // 获取失败
    SAVA_SUCCEED,       // 保存成功
    SAVA_FAILED,        // 保存失败
    REMOVE_SUCCEED,     // 移除成功
    REMOVE_FAILED,      // 移除失败
    LOGIN_SUCCEED,      // 登录成功
    LOGIN_FAILED,       // 登录失败
    LOGOUT_SUCCEED,     // 登出成功
    LOGOUT_FAILED,      // 登出失败
    REGISTER_SUCCEED,   // 注册成功
    REGISTER_FAILED,    // 注册失败
    ERROR               // 异常
}
