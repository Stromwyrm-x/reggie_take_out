package com.weng;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.util.Set;

@SpringBootTest
class ReggieTakeOutApplicationTests
{
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void setRedis()
    {
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        valueOperations.set("name","weng");

        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        stringStringValueOperations.set("name","weng");



    }
    @Test
    void getRedis()
    {
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        System.out.println(valueOperations.get("name"));
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        System.out.println(stringValueOperations.get("name"));

        Set<String> keys = stringRedisTemplate.keys("*");
        for (String key : keys)
        {
            System.out.println(key);
        }

        ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
        zSetOperations.add("myzset","a",1.0);
        zSetOperations.add("myzset","b",2.0);

        zSetOperations.range("myzset",0,-1);

    }



}
