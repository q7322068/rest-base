package com.onecoderspace.base;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Maps;
import com.onecoderspace.base.BaseApplication;
import com.onecoderspace.base.domain.User;
import com.onecoderspace.base.service.UserService;
import com.onecoderspace.base.util.JacksonHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=BaseApplication.class)
public class UserServiceTest {

	@Test
	public void listByPage(){
//		Map<String, String> params = Maps.newHashMap();
//		params.put("type", "0");
//		params.put("status", "1");
//		params.put("username", "te");
//		params.put("name", "");
//		Page<User> page = userService.listByPage(params, new PageRequest(0, 10));	
//		System.err.println(JacksonHelper.toJson(page));
		
		List<User> list = userService.findAll();
		System.err.println(JacksonHelper.toJson(list));
		
		Map<String, Object> params2 = Maps.newHashMap();
		params2.put("type", "0");
		params2.put("status", "");
		params2.put("username:like", "te");
		params2.put("name:like", "");
		
		list = userService.list(params2);
		System.err.println(JacksonHelper.toJson(list));
		
		Page<User> page = userService.list(params2, new PageRequest(0, 10));
		System.err.println(JacksonHelper.toJson(page));
		
	}
	
	@Autowired
	private UserService userService;
	
}
