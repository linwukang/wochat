package com.lwk.wochat.api.data.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwk.wochat.api.ApiApplication;
import com.lwk.wochat.api.data.redis.jedis.JedisMap;
import com.lwk.wochat.api.data.redis.value.RedisHashValue;
import com.lwk.wochat.api.data.serialization.Serializer;
import com.lwk.wochat.api.data.serialization.serializer.StringSerializer;
import com.lwk.wochat.api.pojo.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApiApplication.class)
class RedisMapTest {
    RedisMap<String, Account> redisMap;
    RedisHashValue<String, Account> redisHashValueMap;
    @Autowired
    RedisTemplate<String, String> redisTemplateString;

    @BeforeEach
    public void init() {
        Jedis jedis = new Jedis("localhost", 6379);

        redisMap = new JedisMap<>(
                jedis,
                ":",
                "RedisMapTest",
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

//    @Test
//    public void testPair() {
//        redisMapString.putPair("test001", "test002");
//        assertEquals("test002", redisMapString.getOther("test001"));
//        assertEquals("test001", redisMapString.getOther("test002"));
//        assertEquals("test002", redisMapString.removePair("test001"));
//
//        redisMapString.putPair("test001", "test002");
//        assertEquals("test002", redisMapString.getOther("test001"));
//        assertEquals("test001", redisMapString.getOther("test002"));
//        assertEquals("test001", redisMapString.removePair("test002"));
//
//        redisMapString.putPair("test003", "test003");
//        assertEquals("test003", redisMapString.getOther("test003"));
//        assertEquals("test003", redisMapString.removePair("test003"));
//    }

}