package com.lwk.wochat.api;

import com.lwk.wochat.api.dao.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
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
        assert accountRepository
                .findById(2L)
                .map(account -> account.getPassword().equals("123456")
                                && account.getAccount().equals("114514"))
                .orElse(false);
    }

}
