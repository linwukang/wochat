package com.lwk.wochat.account_service.controller;

import com.lwk.wochat.account_service.service.UserLoginService;
import com.lwk.wochat.api.pojo.entity.Account;
import com.lwk.wochat.api.pojo.http.response.Code;
import com.lwk.wochat.api.pojo.http.response.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/account")
public class LoginController {
    @Resource
    private UserLoginService userLoginService;

    /**
     * 登录
     * @param account 账号信息，包含用户名和密码
     * @return token
     */
    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody Account account) {
        Optional<String> tokenOptional = userLoginService.tryLogin(account);

        return tokenOptional
                .map(Result::getSucceed)
                .orElse(Result.getFailed());
    }

    /**
     * 登出
     * @param account 账号信息，包含用户名
     * @param token token
     * @return 是否成功
     */
    @PostMapping("/logout/{token}")
    public Result<Boolean> logout(@RequestBody Account account, @PathVariable String token) {
        boolean logout = userLoginService.logout(account, token);

        return logout
                ? new Result<>(true, Code.GET_SUCCEED, null)
                : new Result<>(false, Code.GET_FAILED, "登出失败");

    }
}
