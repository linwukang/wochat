package com.lwk.wochat.account_service.service;

import com.lwk.wochat.account_service.AccountServiceApplication;
import com.lwk.wochat.api.pojo.entity.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AccountServiceApplication.class)
class UserLoginServiceTest {

    @Resource(type = UserLoginService.class)
    UserLoginService userLoginService;
    /**
     * 测试 {@link UserLoginService} 的基本功能
     */
    @Test
    void testUserLoginService() {
        /*
          账号       密码
          test0001, aaaaaa
          test0002, bbbbbb
          test0003, cccccc
          test0004, dddddd
         */

        Account account01 = Account.builder().username("test0001").password("aaaaaa").build();
        Account account02 = Account.builder().username("test0002").password("bbbbbb").build();
        Account account03 = Account.builder().username("test0003").password("cccccc").build();
        Account account04 = Account.builder().username("test0004").password("dddddd").build();

        assertFalse(userLoginService.logged(account01));
        assertFalse(userLoginService.logged(account02));
        assertFalse(userLoginService.logged(account03));
        assertFalse(userLoginService.logged(account04));

        Optional<String> tokOptional01 = userLoginService.tryLogin(account01);
        Optional<String> tokOptional02 = userLoginService.tryLogin(account02);
        Optional<String> tokOptional03 = userLoginService.tryLogin(account03);
        Optional<String> tokOptional04 = userLoginService.tryLogin(account04);

        assertTrue(tokOptional01.isPresent());
        assertTrue(tokOptional02.isPresent());
        assertTrue(tokOptional03.isPresent());
        assertTrue(tokOptional04.isPresent());

        String tok01 = tokOptional01.get();
        String tok02 = tokOptional02.get();
        String tok03 = tokOptional03.get();
        String tok04 = tokOptional04.get();

        assertTrue(userLoginService.logged(account01));
        assertTrue(userLoginService.logged(account02));
        assertTrue(userLoginService.logged(account03));
        assertTrue(userLoginService.logged(account04));

        assertTrue(userLoginService.logout(account01, tok01));
        assertTrue(userLoginService.logout(account02, tok02));
        assertTrue(userLoginService.logout(account03, tok03));
        assertTrue(userLoginService.logout(account04, tok04));

        assertFalse(userLoginService.logged(account01));
        assertFalse(userLoginService.logged(account02));
        assertFalse(userLoginService.logged(account03));
        assertFalse(userLoginService.logged(account04));
    }
}