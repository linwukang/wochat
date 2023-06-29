package com.lwk.wochat.account_service.service;

import com.lwk.wochat.account_service.AccountServiceApplication;
import com.lwk.wochat.account_service.service.impl.EmailVerificationCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AccountServiceApplication.class)
class VerificationCodeServiceTest {
    @Resource(type = EmailVerificationCodeService.class)
    VerificationCodeService verificationCodeService;

    @Test
    public void testVerificationCodeService() {
        String code = verificationCodeService.generateVerificationCode(16, "aaa", Duration.ofSeconds(1000));
        assertEquals(16, code.length());
        assertTrue(verificationCodeService.sendVerificationCode(code, Duration.ofSeconds(1000), "1145141919810"));
        assertTrue(verificationCodeService.checkVerificationCode(code, "aaa"));
        verificationCodeService.invalidateVerificationCode(code);
        assertFalse(verificationCodeService.checkVerificationCode(code, "aaa"));
    }

}