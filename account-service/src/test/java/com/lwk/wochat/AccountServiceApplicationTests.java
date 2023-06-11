package com.lwk.wochat;

import com.lwk.wochat.account_service.AccountServiceApplication;
import com.lwk.wochat.account_service.service.TokenService;
import com.lwk.wochat.account_service.service.UserLoginService;
import com.lwk.wochat.api.clients.RedisClient;
import com.lwk.wochat.api.pojo.entity.Account;
import com.lwk.wochat.api.pojo.http.response.Code;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

@SpringBootTest(classes = {
        AccountServiceApplication.class
})
@SuppressWarnings("all")
class AccountServiceApplicationTests {
    @Resource(type = RedisClient.class)
    RedisClient redisClient;

    /**
     * 测试 {@link RedisClient} 的基本功能
     */
//    @Test
    void testRedisClient() {
        Account account1 = new Account(1L, "111", "aaa", null);
        Account account2 = new Account(2L, "222", "bbb", null);
        Account account3 = new Account(3L, "333", "ccc", null);
        Account account4 = new Account(4L, "444", "ddd", null);
        // 增
        Assertions.assertEquals(Code.SAVA_SUCCEED, redisClient.save("test001", account1).getCode());
        Assertions.assertEquals(Code.SAVA_SUCCEED, redisClient.save("test002", account2).getCode());
        Assertions.assertEquals(Code.SAVA_SUCCEED, redisClient.save("test003", account3).getCode());
        Assertions.assertEquals(Code.SAVA_SUCCEED, redisClient.save("test004", account4).getCode());

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
        Assertions.assertEquals(Code.REMOVE_SUCCEED, redisClient.remove("test001").getCode());
        Assertions.assertEquals(Code.REMOVE_SUCCEED, redisClient.remove("test002").getCode());
        Assertions.assertEquals(Code.REMOVE_SUCCEED, redisClient.remove("test003").getCode());
        Assertions.assertEquals(Code.REMOVE_SUCCEED, redisClient.remove("test004").getCode());

        Assertions.assertEquals(Code.GET_FAILED, redisClient.get("test001", Account.class).getCode());
        Assertions.assertEquals(Code.GET_FAILED, redisClient.get("test002", Account.class).getCode());
        Assertions.assertEquals(Code.GET_FAILED, redisClient.get("test003", Account.class).getCode());
        Assertions.assertEquals(Code.GET_FAILED, redisClient.get("test004", Account.class).getCode());

//        redisClient.save("test005", "test005");
//        Assertions.assertEquals(Code.GET_SUCCEED, redisClient.get("test005").code());
//        Assertions.assertEquals("test005", redisClient.get("test005").data().get());
//        Assertions.assertEquals(Code.REMOVE_SUCCEED, redisClient.remove("test005").code());
//        Assertions.assertEquals(Code.REMOVE_FAILED, redisClient.remove("test005").code());
//        Assertions.assertEquals(Code.GET_FAILED, redisClient.get("test005").code());
//
//        redisClient.save("test006", "test006");
    }

    @Resource(type = TokenService.class)
    TokenService tokenService;
    /**
     * 测试 {@link TokenService} 的基本功能
     */
    @Test
    void testTokenService() {
        Account account1 = new Account(1L, "111", "aaa", new Date());
        String token = tokenService.login(account1);

        Optional<String> accountOptional = tokenService.tryGetAccount(token);
        Assertions.assertTrue(accountOptional.isPresent());
        Assertions.assertEquals(account1.getUsername(), accountOptional.get());

        Optional<String> tokenOptional = tokenService.tryGetToken(account1.getUsername());
        Assertions.assertTrue(tokenOptional.isPresent());
        Assertions.assertEquals(token, tokenOptional.get());

        tokenService.invalidateToken(token);

        Assertions.assertFalse(tokenService.tryGetAccount(token).isPresent());
        Assertions.assertFalse(tokenService.tryGetToken(account1.getUsername()).isPresent());
    }


    @Resource(type = UserLoginService.class)
    UserLoginService userLoginService;
    /**
     * 测试 {@link UserLoginService} 的基本功能
     */
    @Test
    void testUserLoginService() {
        /**
         * 账号       密码
         * test0001, aaaaaa
         * test0002, bbbbbb
         * test0003, cccccc
         * test0004, dddddd
         **/

        Account account01 = Account.builder().username("test0001").password("aaaaaa").build();
        Account account02 = Account.builder().username("test0002").password("bbbbbb").build();
        Account account03 = Account.builder().username("test0003").password("cccccc").build();
        Account account04 = Account.builder().username("test0004").password("dddddd").build();

        Assertions.assertFalse(userLoginService.logged(account01));
        Assertions.assertFalse(userLoginService.logged(account02));
        Assertions.assertFalse(userLoginService.logged(account03));
        Assertions.assertFalse(userLoginService.logged(account04));

        Optional<String> tokOptional01 = userLoginService.tryLogin(account01);
        Optional<String> tokOptional02 = userLoginService.tryLogin(account02);
        Optional<String> tokOptional03 = userLoginService.tryLogin(account03);
        Optional<String> tokOptional04 = userLoginService.tryLogin(account04);

        Assertions.assertTrue(tokOptional01.isPresent());
        Assertions.assertTrue(tokOptional02.isPresent());
        Assertions.assertTrue(tokOptional03.isPresent());
        Assertions.assertTrue(tokOptional04.isPresent());

        String tok01 = tokOptional01.get();
        String tok02 = tokOptional02.get();
        String tok03 = tokOptional03.get();
        String tok04 = tokOptional04.get();

        Assertions.assertTrue(userLoginService.logged(account01));
        Assertions.assertTrue(userLoginService.logged(account02));
        Assertions.assertTrue(userLoginService.logged(account03));
        Assertions.assertTrue(userLoginService.logged(account04));

        Assertions.assertTrue(userLoginService.logout(account01, tok01));
        Assertions.assertTrue(userLoginService.logout(account02, tok02));
        Assertions.assertTrue(userLoginService.logout(account03, tok03));
        Assertions.assertTrue(userLoginService.logout(account04, tok04));

        Assertions.assertFalse(userLoginService.logged(account01));
        Assertions.assertFalse(userLoginService.logged(account02));
        Assertions.assertFalse(userLoginService.logged(account03));
        Assertions.assertFalse(userLoginService.logged(account04));
    }
}
