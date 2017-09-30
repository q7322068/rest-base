package com.onecoderspace.base;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=BaseApplication.class)
public class StringRedisTemplateServiceTest {

	@Test
	public void test(){
		//设置缓存，建议每个键都设置过期时间
		redisTemplate.opsForValue().set("test", "test", 10, TimeUnit.SECONDS);
		Assert.assertEquals(redisTemplate.opsForValue().get("test"), "test");
		
		redisTemplate.delete("test");//删除某个键
		
		//操作set
		redisTemplate.opsForSet().add("testSet", "1");
		redisTemplate.opsForSet().add("testSet", "2");
		redisTemplate.opsForSet().add("testSet", "3");
		Set<String> members = redisTemplate.opsForSet().members("testSet");//获取set内的所有值
		for (String string : members) {
			System.err.println(string);
		}
		redisTemplate.opsForSet().remove("testSet", "1","2");//移除set内的多个对象
		
		System.err.println("-----------");
		
		//操作list
		redisTemplate.opsForList().rightPush("testList", "1");
		redisTemplate.opsForList().rightPush("testList", "2");
		redisTemplate.opsForList().rightPush("testList", "3");
		List<String> list = redisTemplate.opsForList().range("testList", 0, -1);
		for (String string : list) {
			System.err.println(string);
		}
	}
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
}

