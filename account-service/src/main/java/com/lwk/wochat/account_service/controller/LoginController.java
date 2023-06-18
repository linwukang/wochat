package com.lwk.wochat.account_service.controller;

import com.lwk.wochat.account_service.service.UserLoginService;
import com.lwk.wochat.api.pojo.entity.Account;
import com.lwk.wochat.api.pojo.http.response.Code;
import com.lwk.wochat.api.pojo.http.response.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Optional;

/**
 * 用于账号登录、登出等操作的 Controller
 */
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
                .map(token -> new Result<>(token, Code.LOGIN_SUCCEED))
                .orElse(new Result<>(Code.LOGIN_FAILED, "登录失败，用户名或密码错误"));
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
                ? new Result<>(true, Code.LOGOUT_SUCCEED)
                : new Result<>(false, Code.LOGIN_FAILED, "登出失败");

    }

    /**
     * 判断用户是否已登录
     * @param username 用户名
     * @return 是否登录
     */
    @GetMapping("/logged/{username}")
    public Result<Boolean> logged(@PathVariable String username) {
        boolean logged = userLoginService.logged(Account.builder().username(username).build());

        return new Result<>(logged, Code.GET_SUCCEED);
    }
}
