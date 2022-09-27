package com.hello.world.spring.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class DemoTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void test1 (){
        redisTemplate.expire("test", 100, TimeUnit.SECONDS);
    }
}
