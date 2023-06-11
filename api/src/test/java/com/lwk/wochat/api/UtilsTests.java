package com.lwk.wochat.api;

import com.lwk.wochat.api.pojo.entity.Account;
import com.lwk.wochat.api.utils.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class UtilsTests {


    /**
     * 测试工具类 {@link BeanUtil}
     */
    @Test
    void testBeanUtil() {
        Account account1 = new Account(0L, "123", "aaa", new Date());
        Map<String, Object> accountMap1 = new HashMap<>();
        accountMap1.put("id", account1.getId());
        accountMap1.put("account", account1.getUsername());
        accountMap1.put("password", account1.getPassword());
        accountMap1.put("createTime", account1.getCreateTime());

        Assert.assertEquals(account1, BeanUtil.mapToBean(accountMap1, Account.class));


        Account account2 = new Account(null, "123", "aaa", null);
        Map<String, Object> accountMap2 = new HashMap<>();
        accountMap2.put("id", account2.getId());
        accountMap2.put("account", account2.getUsername());
        accountMap2.put("password", account2.getPassword());
        accountMap2.put("createTime", account2.getCreateTime());

        Assert.assertEquals(account2, BeanUtil.mapToBean(accountMap2, Account.class));

        Account account3 = new Account(null, "123", "aaa", null);
        Map<String, Object> accountMap3 = new HashMap<>();
        accountMap3.put("id", account3.getId());
        accountMap3.put("account", account3.getUsername());
        accountMap3.put("password", account3.getPassword());
        accountMap3.put("createTime", account3.getCreateTime());
        accountMap3.put("testKey01", "testValue01");
        accountMap3.put("testKey02", "testValue02");
        accountMap3.put("testKey03", "testValue03");

        Assert.assertEquals(account3, BeanUtil.mapToBean(accountMap3, Account.class));

        Account account4 = new Account(5L, "123", "aaa", new Date());
        Map<String, Object> accountMap4 = new HashMap<>();
        accountMap4.put("id", account4.getId());

        Assert.assertEquals(
                new Account(5L, null, null, null),
                BeanUtil.mapToBean(accountMap4, Account.class));

        String s = "test BeanMap.mapToBean";
        Assert.assertEquals(s, BeanUtil.mapToBean(s, String.class));

    }

}
