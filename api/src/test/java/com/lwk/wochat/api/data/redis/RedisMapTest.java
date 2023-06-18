package com.lwk.wochat.api.data.redis;

import com.lwk.wochat.api.ApiApplication;
import com.lwk.wochat.api.configuration.RedisConfiguration;
import com.lwk.wochat.api.data.redis.value.impl.RedisHashValueImpl;
import com.lwk.wochat.api.pojo.entity.Account;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApiApplication.class)
class RedisMapTest {
    @Resource
    RedisConfiguration.RedisTemplateFactory<Account> redisTemplateFactory;

    RedisTemplate<String, Account> redisTemplate;
    RedisTemplateMap<String, Account> redisMap;
    RedisHashValueImpl<String, Account> redisHashValueMap;

    @Before
    public void init() {
        redisTemplate = redisTemplateFactory.create(Account.class);
        redisMap = new RedisTemplateMap<>(redisTemplate, "RedisMapTest:");
        redisHashValueMap = new RedisHashValueImpl<>("RedisHashValueMapTest", redisTemplate.opsForHash());
    }

    @Test
    public void testRedisTemplateMap() {
        init();

        Account account1 = new Account(1L, "aaa", "aaaa", null);
        Account account2 = new Account(2L, "bbb", "bbbb", null);
        Account account3 = new Account(3L, "ccc", "cccc", null);
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
    public void testRedisTemplateMap001() {
        init();
        int count = 100_000;
        for (int i = 0; i < count; i++) {
            redisMap.put("" + i, new Account((long) i, UUID.randomUUID().toString(), UUID.randomUUID().toString(), new Date()));
        }
        assertEquals(count, redisMap.size());
        Set<String> keys = redisMap.keySet();

        redisMap.clear();
        assertTrue(redisMap.isEmpty());
    }

}