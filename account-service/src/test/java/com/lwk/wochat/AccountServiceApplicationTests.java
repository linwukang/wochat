package com.lwk.wochat;

import com.lwk.wochat.api.ApiApplication;
import com.lwk.wochat.api.clients.RedisClient;
import com.lwk.wochat.api.pojo.entity.Account;
import com.lwk.wochat.api.pojo.http.response.Code;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.beans.BeanMap;

import javax.annotation.Resource;
import java.util.Map;

@SpringBootTest(classes = {
        AccountServiceApplicationTests.class,
        ApiApplication.class
})
@SuppressWarnings("all")
class AccountServiceApplicationTests {
    @Resource(type = RedisClient.class)
    RedisClient redisClient;

    /**
     * 测试 RedisClient 的基本功能
     */
    @Test
    void testRedisClient() {
        Account account1 = new Account(1L, "111", "aaa", null);
        Account account2 = new Account(2L, "222", "bbb", null);
        Account account3 = new Account(3L, "333", "ccc", null);
        Account account4 = new Account(4L, "444", "ddd", null);
        // 增
        Assertions.assertEquals(Code.SAVA_SUCCEED, redisClient.save("test001", account1).code());
        Assertions.assertEquals(Code.SAVA_SUCCEED, redisClient.save("test002", account2).code());
        Assertions.assertEquals(Code.SAVA_SUCCEED, redisClient.save("test003", account3).code());
        Assertions.assertEquals(Code.SAVA_SUCCEED, redisClient.save("test004", account4).code());

        // 查
        Map<String, Object> getAccount1 = (Map<String, Object>) redisClient.get("test001").data().get();
        Map<String, Object> getAccount2 = (Map<String, Object>) redisClient.get("test002").data().get();
        Map<String, Object> getAccount3 = (Map<String, Object>) redisClient.get("test003").data().get();
        Map<String, Object> getAccount4 = (Map<String, Object>) redisClient.get("test004").data().get();

        Assertions.assertEquals(account1.getId(), Long.valueOf((Integer) getAccount1.get("id")));
        Assertions.assertEquals(account1.getAccount(), (getAccount1.get("account")));
        Assertions.assertEquals(account1.getPassword(), (getAccount1.get("password")));
        Assertions.assertEquals(account1.getCreateTime(), (getAccount1.get("createTime")));

        Assertions.assertEquals(account2.getId(), Long.valueOf((Integer) getAccount2.get("id")));
        Assertions.assertEquals(account2.getAccount(), (getAccount2.get("account")));
        Assertions.assertEquals(account2.getPassword(), (getAccount2.get("password")));
        Assertions.assertEquals(account2.getCreateTime(), (getAccount2.get("createTime")));

        Assertions.assertEquals(account3.getId(), Long.valueOf((Integer) getAccount3.get("id")));
        Assertions.assertEquals(account3.getAccount(), (getAccount3.get("account")));
        Assertions.assertEquals(account3.getPassword(), (getAccount3.get("password")));
        Assertions.assertEquals(account3.getCreateTime(), (getAccount3.get("createTime")));

        Assertions.assertEquals(account4.getId(), Long.valueOf((Integer) getAccount4.get("id")));
        Assertions.assertEquals(account4.getAccount(), (getAccount4.get("account")));
        Assertions.assertEquals(account4.getPassword(), (getAccount4.get("password")));
        Assertions.assertEquals(account4.getCreateTime(), (getAccount4.get("createTime")));

        // 删
        Assertions.assertEquals(Code.REMOVE_SUCCEED, redisClient.remove("test001").code());
        Assertions.assertEquals(Code.REMOVE_SUCCEED, redisClient.remove("test002").code());
        Assertions.assertEquals(Code.REMOVE_SUCCEED, redisClient.remove("test003").code());
        Assertions.assertEquals(Code.REMOVE_SUCCEED, redisClient.remove("test004").code());

        Assertions.assertEquals(Code.GET_FAILED, redisClient.get("test001").code());
        Assertions.assertEquals(Code.GET_FAILED, redisClient.get("test002").code());
        Assertions.assertEquals(Code.GET_FAILED, redisClient.get("test003").code());
        Assertions.assertEquals(Code.GET_FAILED, redisClient.get("test004").code());
    }

}
