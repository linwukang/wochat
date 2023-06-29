package com.lwk.wochat.api;

import com.lwk.wochat.api.dao.repository.AccountRepository;
import com.lwk.wochat.api.data._cache.RedisCache;
import com.lwk.wochat.api.data._cache.factory.RedisCacheFactory;
import com.lwk.wochat.api.data.crud.Crud;
import com.lwk.wochat.api.pojo.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CacheTests {

    @Resource(type = RedisCacheFactory.class)
    RedisCacheFactory<Account> redisCacheFactory;

    @Resource
    AccountRepository accountRepository;

    Crud<String, Account> crud;

    RedisCache<Account> cache;

    @Before
    public void init() {
        crud = new Crud<>() {
            @Override
            public void create(Account entity) {
                accountRepository.save(entity);
            }

            @Override
            public void delete(String key) {
                accountRepository.delete(new Account(null, key, null, null, null));
            }

            @Override
            public Account retrieve(String key) {
                Optional<Account> account = accountRepository.findById(Long.parseLong(key));
                return account.orElseThrow();
            }

            @Override
            public void update(String key, Account entity) {
                entity.setUsername(key);
                accountRepository.save(entity);
            }
        };

        cache = (RedisCache<Account>) redisCacheFactory.create("cacheTest", crud);
    }


    @Test
    public void testRedisCache() {
        Account account = new Account(7L, "testRedisCache0001", "aaaaaa", new Date(), null);
//        System.out.println(account);

        cache.set(account.getId().toString(), account, Account.class);
        var account1 = cache.get(String.valueOf(7L), Account.class);
        long times = 10000;

        Date start1 = new Date();
        for (int i = 0; i < times; i++) {
            cache.get("7", Account.class);
        }
        Date end1 = new Date();

        System.out.println("cache: " + (end1.getTime() - start1.getTime()) / 1000.0 + " s");

        Date start2 = new Date();
        for (int i = 0; i < times; i++) {
            accountRepository.findById(7L);
        }
        Date end2 = new Date();

        System.out.println("repository: " + (end2.getTime() - start2.getTime()) / 1000.0 + " s");




        System.out.println(account1);


    }
}
