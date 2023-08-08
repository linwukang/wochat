package com.lwk.wochat;

import com.lwk.wochat.account_service.AccountServiceApplication;
import com.lwk.wochat.api.clients.RedisClient;
import com.lwk.wochat.api.pojo.entity.Account;
import com.lwk.wochat.api.pojo.http.response.Code;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes = AccountServiceApplication.class)
@SuppressWarnings("all")
class AccountServiceApplicationTests {
    @Resource(type = RedisClient.class)
    RedisClient redisClient;

    /**
     * 测试 {@link RedisClient} 的基本功能
     */
//    @Test
    void testRedisClient() {
        Account account1 = new Account(1L, "111", "aaa", null, null);
        Account account2 = new Account(2L, "222", "bbb", null, null);
        Account account3 = new Account(3L, "333", "ccc", null, null);
        Account account4 = new Account(4L, "444", "ddd", null, null);
        // 增
        Assertions.assertEquals(Code.OK, redisClient.save("test001", account1).getCode());
        Assertions.assertEquals(Code.OK, redisClient.save("test002", account2).getCode());
        Assertions.assertEquals(Code.OK, redisClient.save("test003", account3).getCode());
        Assertions.assertEquals(Code.OK, redisClient.save("test004", account4).getCode());

        // 查
        Account getAccount1 = redisClient.get("test001", Account.class).getData().get();
        Account getAccount2 = redisClient.get("test002", Account.class).getData().get();
        Account getAccount3 = redisClient.get("test003", Account.class).getData().get();
        Account getAccount4 = redisClient.get("test004", Account.class).getData().get();

        Assertions.assertEquals(account1, getAccount1);
        Assertions.assertEquals(account2, getAccount2);
        Assertions.assertEquals(account3, getAccount3);
        Assertions.assertEquals(account4, getAccount4);

        // 删
        Assertions.assertEquals(Code.BAD_REQUEST, redisClient.remove("test001").getCode());
        Assertions.assertEquals(Code.BAD_REQUEST, redisClient.remove("test002").getCode());
        Assertions.assertEquals(Code.BAD_REQUEST, redisClient.remove("test003").getCode());
        Assertions.assertEquals(Code.BAD_REQUEST, redisClient.remove("test004").getCode());

        Assertions.assertEquals(Code.BAD_REQUEST, redisClient.get("test001", Account.class).getCode());
        Assertions.assertEquals(Code.BAD_REQUEST, redisClient.get("test002", Account.class).getCode());
        Assertions.assertEquals(Code.BAD_REQUEST, redisClient.get("test003", Account.class).getCode());
        Assertions.assertEquals(Code.BAD_REQUEST, redisClient.get("test004", Account.class).getCode());

//        redisClient.save("test005", "test005");
//        Assertions.assertEquals(Code.GET_SUCCEED, redisClient.get("test005").code());
//        Assertions.assertEquals("test005", redisClient.get("test005").data().get());
//        Assertions.assertEquals(Code.REMOVE_SUCCEED, redisClient.remove("test005").code());
//        Assertions.assertEquals(Code.REMOVE_FAILED, redisClient.remove("test005").code());
//        Assertions.assertEquals(Code.GET_FAILED, redisClient.get("test005").code());
//
//        redisClient.save("test006", "test006");
    }

}
