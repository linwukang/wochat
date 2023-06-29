package com.lwk.wochat.api.data.redis.value;

import com.lwk.wochat.api.ApiApplication;
import com.lwk.wochat.api.configuration.RedisConfiguration;
import com.lwk.wochat.api.data.redis.RedisTemplateMap;
import com.lwk.wochat.api.data.redis.value.impl.RedisHashValueImpl;
import com.lwk.wochat.api.pojo.entity.Account;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApiApplication.class)
class RedisValueMapTest {
    @Resource
    RedisConfiguration.RedisTemplateFactory<Account> redisTemplateFactory;

    RedisTemplate<String, Account> redisTemplate;
    RedisTemplateMap<String, Account> redisMap;
    RedisHashValueImpl<String, Account> redisHashValueMap;

    @Before
    public void init() {
        redisTemplate = redisTemplateFactory.create(Account.class);
        redisMap = new RedisTemplateMap<>(redisTemplate, "RedisMapTest:");
        redisHashValueMap = new RedisHashValueImpl<>("RedisValueMapTest", redisTemplate.opsForHash());
    }

    @Test
    public void testRedisHashValueMap() {
        init();

        Account account1 = new Account(1L, "aaa", "aaaa", null, null);
        Account account2 = new Account(2L, "bbb", "bbbb", null, null);
        Account account3 = new Account(3L, "ccc", "cccc", null, null);

        assertTrue(redisHashValueMap.isEmpty());

        redisHashValueMap.put("1", account1);
        assertEquals(1, redisHashValueMap.size());
        assertEquals(Set.of("1"), redisHashValueMap.keySet());
        assertFalse(redisHashValueMap.isEmpty());

        redisHashValueMap.put("2", account2);
        assertEquals(2, redisHashValueMap.size());
        assertEquals(Set.of("1", "2"), redisHashValueMap.keySet());
        assertFalse(redisHashValueMap.isEmpty());

        redisHashValueMap.put("3", account3);
        assertEquals(3, redisHashValueMap.size());
        assertEquals(Set.of("1", "2", "3"), redisHashValueMap.keySet());
        assertFalse(redisHashValueMap.isEmpty());

        assertEquals(account1, redisHashValueMap.get("1"));
        assertEquals(account2, redisHashValueMap.get("2"));
        assertEquals(account3, redisHashValueMap.get("3"));

        assertEquals(account2, redisHashValueMap.remove("2"));
        assertEquals(2, redisHashValueMap.size());
        assertEquals(Set.of("1", "3"), redisHashValueMap.keySet());

        redisHashValueMap.clear();
        assertTrue(redisHashValueMap.isEmpty());
        assertTrue(redisHashValueMap.size() == 0);
    }
}