package com.lwk.wochat.account_service.service;

import com.lwk.wochat.account_service.AccountServiceApplication;
import com.lwk.wochat.api.pojo.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AccountServiceApplication.class)
class RegistrationServiceTest {
    @Resource
    RegistrationService registrationService;
    @Resource
    UserLoginService userLoginService;

    @Test
    public void testRegistrationService() {
        Account account = Account
                .builder()
                .username("testRegistrationService01")
                .password("testRegistrationService01")
                .build();

        if (registrationService.accountExisted(account)) {
            assertTrue(registrationService.deregister(account));
        }

        Optional<Account> accountOptional = registrationService.tryRegister(account);

        assertTrue(accountOptional.isPresent());
        assertEquals(account.getUsername(), accountOptional.get().getUsername());
        assertEquals(account.getPassword(), accountOptional.get().getPassword());

        account = accountOptional.get();
        Optional<String> tokenOptional = userLoginService.tryLogin(account);
        assertTrue(tokenOptional.isPresent());
        String token = tokenOptional.get();
        assertTrue(userLoginService.logged(account));
        assertTrue(userLoginService.logout(account, token));
        assertFalse(userLoginService.logged(account));

        assertTrue(registrationService.deregister(account));
        assertFalse(registrationService.accountExisted(account));
    }
}