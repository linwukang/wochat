package com.lwk.wochat.api.data.redis.value;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwk.wochat.api.ApiApplication;
import com.lwk.wochat.api.data.redis.RedisMap;
import com.lwk.wochat.api.data.redis.jedis.JedisMap;
import com.lwk.wochat.api.data.serialization.Serializer;
import com.lwk.wochat.api.data.serialization.serializer.StringSerializer;
import com.lwk.wochat.api.pojo.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApiApplication.class)
class RedisValueMapTest {
    RedisMap<String, Account> redisMap;

    RedisHashValue<String, Account> redisHashValueMap;

    @BeforeEach
    public void init() {
        Jedis jedis = new Jedis("localhost", 6379);

        redisMap = new JedisMap<>(
                jedis,
                ":",
                "RedisValueMapTest",
                new StringSerializer(),
                new Serializer<>() {
                    @Override
                    public byte[] serialize(Account object) {
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            return mapper.writeValueAsString(object).getBytes();
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public Account deserialize(byte[] serialized) {
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            return mapper.readValue(serialized, Account.class);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
        redisHashValueMap = redisMap.hashWith("hashTest");
    }

    @Test
    public void testRedisHashValueMap() {
        init();

        Account account1 = new Account(1L, "aaa", "aaaa", null, null, null);
        Account account2 = new Account(2L, "bbb", "bbbb", null, null, null);
        Account account3 = new Account(3L, "ccc", "cccc", null, null, null);

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
        assertEquals(0, redisHashValueMap.size());
    }
}