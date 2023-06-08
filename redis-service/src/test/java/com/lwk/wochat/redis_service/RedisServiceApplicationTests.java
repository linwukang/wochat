package com.lwk.wochat.redis_service;

import com.lwk.wochat.api.pojo.entity.Account;
import com.lwk.wochat.api.pojo.http.response.Code;
import com.lwk.wochat.redis_service.service.RedisPlusService;
import com.lwk.wochat.redis_service.service.RedisService;
import com.lwk.wochat.redis_service.utils.StringConstant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
@SuppressWarnings("all")
class RedisServiceApplicationTests {

    @Autowired
    RedisService<Account> redisService;

    @Autowired
    RedisService<String> redisServiceString;

    /**
     * 测试 {@link RedisService} 的基本功能
     */
    @Test
    void testRedisService() {
        Account account1 = new Account(1L, "111", "aaa", null);
        Account account2 = new Account(2L, "222", "bbb", null);
        Account account3 = new Account(3L, "333", "ccc", null);
        Account account4 = new Account(4L, "444", "ddd", null);
        // 增
        redisService.setByKey("test001", account1);
        redisService.setByKey("test002", account2);
        redisService.setByKey("test003", account3);
        redisService.setByKey("test004", account4);

        // 查
        Account getAccount1 = redisService.getByKey("test001", Account.class).get();
        Account getAccount2 = redisService.getByKey("test002", Account.class).get();
        Account getAccount3 = redisService.getByKey("test003", Account.class).get();
        Account getAccount4 = redisService.getByKey("test004", Account.class).get();

        Assertions.assertEquals(account1, getAccount1);
        Assertions.assertEquals(account2, getAccount2);
        Assertions.assertEquals(account3, getAccount3);
        Assertions.assertEquals(account4, getAccount4);

        // 删
        Assertions.assertTrue(redisService.removeKey("test001"));
        Assertions.assertTrue(redisService.removeKey("test002"));
        Assertions.assertTrue(redisService.removeKey("test003"));
        Assertions.assertTrue(redisService.removeKey("test004"));

        Assertions.assertFalse(redisService.getByKey("test001", Account.class).isPresent());
        Assertions.assertFalse(redisService.getByKey("test002", Account.class).isPresent());
        Assertions.assertFalse(redisService.getByKey("test003", Account.class).isPresent());
        Assertions.assertFalse(redisService.getByKey("test004", Account.class).isPresent());

        redisServiceString.setByKey("test005", "test005");
        Assertions.assertTrue(redisServiceString.getByKey("test005", String.class).isPresent());
        Assertions.assertEquals("test005", redisServiceString.getByKey("test005", String.class).get());
        Assertions.assertTrue(redisServiceString.removeKey("test005"));
        Assertions.assertFalse(redisServiceString.removeKey("test005"));
        Assertions.assertFalse(redisServiceString.getByKey("test005", String.class).isPresent());
    }

    @Autowired
    RedisPlusService<Account> redisPlusService;

    @Autowired
    RedisPlusService<String> redisPlusServiceString;

    @Test
    void testRedisPlusService() {
        Account account1 = new Account(1L, "111", "aaa", null);
        Account account2 = new Account(2L, "222", "bbb", null);
        Account account3 = new Account(3L, "333", "ccc", null);
        Account account4 = new Account(4L, "444", "ddd", null);
        // 增
        redisPlusService.setByKey("test001", account1);
        redisPlusService.setByKey("test002", account2);
        redisPlusService.setByKey("test003", account3);
        redisPlusService.setByKey("test004", account4);

        // 查
        Account getAccount1 = redisPlusService.getByKey("test001", Account.class).get();
        Account getAccount2 = redisPlusService.getByKey("test002", Account.class).get();
        Account getAccount3 = redisPlusService.getByKey("test003", Account.class).get();
        Account getAccount4 = redisPlusService.getByKey("test004", Account.class).get();

        Assertions.assertEquals(account1, getAccount1);
        Assertions.assertEquals(account2, getAccount2);
        Assertions.assertEquals(account3, getAccount3);
        Assertions.assertEquals(account4, getAccount4);

        // 逻辑删除
        Assertions.assertTrue(redisPlusService.markRemoved("test001"));
        Assertions.assertTrue(redisPlusService.markRemoved("test002"));

        Assertions.assertTrue(redisPlusService.isMarkRemoved("test001"));
        Assertions.assertTrue(redisPlusService.isMarkRemoved("test002"));
        Assertions.assertFalse(redisPlusService.isMarkRemoved("test003"));
        Assertions.assertFalse(redisPlusService.isMarkRemoved("test004"));

        // 删
        Assertions.assertTrue(redisPlusService.removeKey(StringConstant.MarkRemovedPrefix + "test001"));
        Assertions.assertTrue(redisPlusService.removeKey(StringConstant.MarkRemovedPrefix + "test002"));
        Assertions.assertTrue(redisPlusService.removeKey("test003"));
        Assertions.assertTrue(redisPlusService.removeKey("test004"));

        Assertions.assertFalse(redisPlusService.getByKey("test001", Account.class).isPresent());
        Assertions.assertFalse(redisPlusService.getByKey("test002", Account.class).isPresent());
        Assertions.assertFalse(redisPlusService.getByKey("test003", Account.class).isPresent());
        Assertions.assertFalse(redisPlusService.getByKey("test004", Account.class).isPresent());


        redisPlusServiceString.setByKey("test005k", "test005v");
        Assertions.assertTrue(redisPlusServiceString.getByKey("test005k").isPresent());
        Assertions.assertEquals("test005v", redisPlusServiceString.getByKey("test005k").get());
        Assertions.assertTrue(redisPlusServiceString.removeKey("test005k"));
        Assertions.assertFalse(redisPlusServiceString.removeKey("test005k"));
        Assertions.assertFalse(redisPlusServiceString.getByKey("test005k").isPresent());
    }

}
