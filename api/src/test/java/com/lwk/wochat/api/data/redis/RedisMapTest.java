package com.lwk.wochat.api.data.redis;

import com.lwk.wochat.api.ApiApplication;
import com.lwk.wochat.api.configuration.RedisConfiguration;
import com.lwk.wochat.api.data.redis.value.impl.RedisHashValueImpl;
import com.lwk.wochat.api.pojo.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApiApplication.class)
class RedisMapTest {
    @Resource
    RedisConfiguration.RedisTemplateFactory<Account> redisTemplateFactory;

    @Resource
    RedisConfiguration.RedisTemplateFactory<String> redisTemplateStringFactory;

    RedisTemplate<String, Account> redisTemplate;
    RedisTemplateMap<String, Account> redisMap;
    RedisMap<String, String> redisMapString;
    RedisHashValueImpl<String, Account> redisHashValueMap;
    @Autowired
    RedisTemplate<String, String> redisTemplateString;

    @BeforeEach
    public void setUp() {
        redisTemplate = redisTemplateFactory.create(Account.class);
        redisMap = new RedisTemplateMap<>(redisTemplate, "RedisMapTest:");
        redisHashValueMap = new RedisHashValueImpl<>("RedisHashValueMapTest", redisTemplate.opsForHash());

        redisMapString = new RedisTemplateMap<>(redisTemplateString, "testPair:");
    }

    @Test
    public void testRedisTemplateMap() {
        Account account1 = Account.builder().id(1L).username("aaa").password("aaaa").build();
        Account account2 = Account.builder().id(2L).username("bbb").password("bbbb").build();
        Account account3 = Account.builder().id(3L).username("ccc").password("cccc").build();
        assertEquals(account1, redisMap.put("test001", account1));
        assertEquals(account2, redisMap.put("test002", account2));
        assertEquals(account3, redisMap.put("test003", account3));

        assertEquals(account1, redisMap.get("test001"));
        assertEquals(account2, redisMap.get("test002"));
        assertEquals(account3, redisMap.get("test003"));
        assertFalse(redisMap.isEmpty());

        assertEquals(3, redisMap.size());

        assertEquals(Set.of("test001", "test002", "test003"), redisMap.keySet());
        assertEquals(Set.of(account1, account2, account3), redisMap.values());

        assertEquals(account1, redisMap.remove("test001"));
        assertEquals(2, redisMap.size());
        assertNull(redisMap.get("test001"));
        assertNull(redisMap.remove("test001"));
        assertFalse(redisMap.isEmpty());

        assertEquals(account2, redisMap.remove("test002"));
        assertEquals(account3, redisMap.remove("test003"));
        assertEquals(0, redisMap.size());
        assertTrue(redisMap.isEmpty());

    }

    @Test
    public void testPair() {
        redisMapString.putPair("test001", "test002");
        assertEquals("test002", redisMapString.getOther("test001"));
        assertEquals("test001", redisMapString.getOther("test002"));
        assertEquals("test002", redisMapString.removePair("test001"));

        redisMapString.putPair("test001", "test002");
        assertEquals("test002", redisMapString.getOther("test001"));
        assertEquals("test001", redisMapString.getOther("test002"));
        assertEquals("test001", redisMapString.removePair("test002"));

        redisMapString.putPair("test003", "test003");
        assertEquals("test003", redisMapString.getOther("test003"));
        assertEquals("test003", redisMapString.removePair("test003"));
    }

}