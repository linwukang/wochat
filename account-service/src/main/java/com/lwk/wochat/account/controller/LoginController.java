package com.lwk.wochat.account.controller;

import com.lwk.wochat.account.service.UserLoginService;
import com.lwk.wochat.api.pojo.entity.Account;
import com.lwk.wochat.api.pojo.http.response.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Optional;

/**
 * 用于账号登录、登出等操作的 Controller
 */
@RestController
@RequestMapping("/account-login")
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
        Optional<String> tokenOptional = userLoginService.tryLogin(account.getUsername(), account.getPassword());

        return tokenOptional
                .map(token -> Result.ok(token))
                .orElse(Result.badRequest( "登录失败，用户名或密码错误"));
    }

    /**
     * 登出
     * @param userId 用户 id
     * @param token token
     * @return 是否成功
     */
    @GetMapping("/logout/{userId}/{token}")
    public Result<Boolean> logout(@PathVariable long userId, @PathVariable String token) {
        boolean logout = userLoginService.logout(userId, token);

        return logout
                ? Result.ok(true)
                : Result.badRequest(false, "登出失败");

    }

    /**
     * 判断用户是否已登录
     * @param id 用户 id
     * @return 是否登录
     */
    @GetMapping("/logged/{id}")
    public Result<Boolean> logged(@PathVariable long id) {
        boolean logged = userLoginService.logged(id);

//        return new Result<>(logged, Code.OK);
        return Result.ok(logged);
    }
}
