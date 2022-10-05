package com.cloud.deemo;


import com.cloud.sys.UserEntity;
import com.cloud.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Description: RedisTest
 * @Author lenovo
 * @Date: 2021/5/8 14:36
 * @Version 1.0
 */
@SpringBootTest
public class RedisTest {
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;
    @Autowired
    //@Qualifier("redisTemplate")
    private RedisUtil redisUtil;
    @Test
    public void test1(){
        redisTemplate.opsForValue().set("test","哈哈哈哈");
        //redisUtil.move("",0);
    }

    @Test
    public void test2(){
        UserEntity entity = new UserEntity(1L,"张三","11","22","asdf",111L);

        // redisTemplate.opsForValue().set("user",entity);
        UserEntity user = (UserEntity)redisTemplate.opsForValue().get("user");
        System.out.println(user);
    }
}
