package com.lwk.wochat.api;

import com.lwk.wochat.api.dao.repository.AccountRepository;
import com.lwk.wochat.api.dao.repository.UserInfoRepository;
import com.lwk.wochat.api.pojo.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class ApiApplicationTests {


    @Test
    void testAccountRepository(@Autowired AccountRepository accountRepository) {
        Assertions.assertTrue(accountRepository
                .findById(2L)
                .map(account -> account.getPassword().equals("123456")
                        && account.getUsername().equals("114514"))
                .orElse(false));
    }

    @Test
    void testUserInfoRepository(
            @Autowired UserInfoRepository userInfoRepository,
            @Autowired AccountRepository accountRepository) {
        UserInfo userInfo = userInfoRepository.findById(1L).orElse(null);
        Assertions.assertNotNull(userInfo);
        Assertions.assertEquals(
                userInfo.getAccount(),
                accountRepository.findById(userInfo.getAccount().getId()).orElse(null));
    }
}
