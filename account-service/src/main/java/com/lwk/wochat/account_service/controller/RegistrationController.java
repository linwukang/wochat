package com.lwk.wochat.account_service.controller;

import com.lwk.wochat.account_service.service.RegistrationService;
import com.lwk.wochat.account_service.service.VerificationCodeService;
import com.lwk.wochat.api.pojo.entity.Account;
import com.lwk.wochat.api.pojo.http.response.Code;
import com.lwk.wochat.api.pojo.http.response.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 用于账号注册操作的 Controller
 */
@RestController
@RequestMapping("/account-registration")
public class RegistrationController {
    private final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Resource
    private RegistrationService registrationService;

    @Resource
    private VerificationCodeService verificationCodeService;

    public RegistrationController() {
    }

    /**
     * 判断用户名是否已被使用
     * @param username 用户名
     * @return 是否被使用
     */
    @GetMapping("/exists-username/{username}")
    public Result<Boolean> existsUsername(@PathVariable String username) {
        boolean existed = registrationService.accountExisted(Account.builder().username(username).build());

        return new Result<>(
                existed,
                Code.OK);
    }

    /**
     * 注册账号
     * @param account 账号信息，包含用户名和密码
     * @param verificationCode 验证码
     * @return 注册是否成功
     */
    @PostMapping("/register/{verificationCode}")
    public synchronized Result<Account> register(@RequestBody Account account, @PathVariable String verificationCode) {
        Result<Boolean> usernameExisted = existsUsername(account.getUsername());
        if (usernameExisted.getData().orElse(false)) {
            return Result.badRequest("用户名已存在");
        }

        boolean verified = verificationCodeService.checkVerificationCode(verificationCode, account.getUsername());
        if (verified) {
            return Result.badRequest("验证码错误");
        }

        Optional<Account> accountOptional = registrationService.tryRegister(account);
        accountOptional.ifPresent(acc -> logger.info("register: account=" + acc));

        return accountOptional
                .map(acc -> Result.ok(
                        Account
                                .builder()
                                .id(acc.getId())
                                .username(acc.getUsername())
                                .build()))
                .orElse(Result.badRequest("注册失败"));
    }
}
