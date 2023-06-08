package com.lwk.wochat.api;

import com.lwk.wochat.api.configuration.RedisConfiguration;
import com.lwk.wochat.api.data.redis.RedisMap;
import com.lwk.wochat.api.pojo.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisMapTests {

    @Resource
    RedisConfiguration.RedisTemplateFactory<Account> redisTemplateFactory;

    RedisTemplate<String, Account> redisTemplate;

    RedisMap<Account> redisMap;

    @Before
    public void init() {
        redisTemplate = redisTemplateFactory.create(Account.class);
        redisMap = new RedisMap<>(redisTemplate, "RedisMapTests:");
    }

    @Test
    public void testMap() {
        Account account1 = new Account(1L, "aaa", "aaaa", null);
        Account account2 = new Account(2L, "bbb", "bbbb", null);
        Account account3 = new Account(3L, "ccc", "cccc", null);
        Assertions.assertEquals(account1, redisMap.put("test001", account1));
        Assertions.assertEquals(account2, redisMap.put("test002", account2));
        Assertions.assertEquals(account3, redisMap.put("test003", account3));

        Assertions.assertEquals(account1, redisMap.get("test001"));
        Assertions.assertEquals(account2, redisMap.get("test002"));
        Assertions.assertEquals(account3, redisMap.get("test003"));
        Assertions.assertFalse(redisMap.isEmpty());

        Assertions.assertEquals(3, redisMap.size());

        Assertions.assertEquals(Set.of("test001", "test002", "test003"), redisMap.keySet());
        Assertions.assertEquals(Set.of(account1, account2, account3), redisMap.values());

        Assertions.assertEquals(account1, redisMap.remove("test001"));
        Assertions.assertEquals(2, redisMap.size());
        Assertions.assertNull(redisMap.get("test001"));
        Assertions.assertNull(redisMap.remove("test001"));
        Assertions.assertFalse(redisMap.isEmpty());

        Assertions.assertEquals(account2, redisMap.remove("test002"));
        Assertions.assertEquals(account3, redisMap.remove("test003"));
        Assertions.assertEquals(0, redisMap.size());
        Assertions.assertTrue(redisMap.isEmpty());

    }


}
