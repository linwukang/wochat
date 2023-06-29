package com.lwk.wochat.account_service.service;

import com.lwk.wochat.account_service.AccountServiceApplication;
import com.lwk.wochat.api.pojo.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AccountServiceApplication.class)
class TokenServiceTest {
    @Resource(type = LoginTokenService.class)
    LoginTokenService tokenService;

    /**
     * 测试 {@link LoginTokenService} 的基本功能
     */
    @Test
    void testTokenService() {
        Account account1 = new Account(1L, "111", "aaa", new Date(), null);
        String token = tokenService.login(account1);

        Optional<String> accountOptional = tokenService.tryGetAccount(token);
        assertTrue(accountOptional.isPresent());
        assertEquals(account1.getUsername(), accountOptional.get());

        Optional<String> tokenOptional = tokenService.tryGetToken(account1.getUsername());
        assertTrue(tokenOptional.isPresent());
        assertEquals(token, tokenOptional.get());

        tokenService.invalidateToken(token);

        assertFalse(tokenService.tryGetAccount(token).isPresent());
        assertFalse(tokenService.tryGetToken(account1.getUsername()).isPresent());
    }

}