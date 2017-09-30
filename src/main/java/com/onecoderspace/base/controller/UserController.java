package com.onecoderspace.base.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.onecoderspace.base.domain.User;
import com.onecoderspace.base.service.UserService;
import com.onecoderspace.base.util.Return;
import com.onecoderspace.base.util.SaltMD5Util;

@Api(value="用户管理",tags={"用户管理"})
@RestController
@RequestMapping("/user")
public class UserController {
	
	@ApiOperation(value="获取用户详细信息", notes="根据ID查找用户")
    @ApiImplicitParam(paramType="query",name = "id", value = "用户ID", required = true,dataType="int")
	@RequiresPermissions(value={"user:get"}) 
	@RequestMapping(value="/get",method=RequestMethod.GET)
	public User get(int id){
		User entity = userService.findById(id);
		entity.setPwd(null);
		entity.setSalt(null);
		return entity;
	}	

	@ApiOperation(value="修改密码", notes="修改密码")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "oldPwd", value = "旧密码", required = true, dataType = "String"),
		@ApiImplicitParam(paramType = "query", name = "pwd", value = "新密码", required = true, dataType = "String"),
		@ApiImplicitParam(paramType = "query", name = "confirmPwd", value = "新密码(确认)", required = true, dataType = "String")})
	@RequiresPermissions(value={"user:reset-pwd"})
	@RequestMapping(value="/reset-pwd",method=RequestMethod.POST)
	public Return resetPwd(String oldPwd,String pwd,String confirmPwd){
		if(StringUtils.isBlank(oldPwd) || StringUtils.isBlank(pwd)
				|| StringUtils.isBlank(confirmPwd) || !pwd.equals(confirmPwd)) {
			return Return.fail("非法参数");
		}
		
		Subject currentUser = SecurityUtils.getSubject();
		Integer userId=(Integer) currentUser.getPrincipal();
		User entity = userService.findById(userId);
		if(!entity.getPwd().equals(SaltMD5Util.encode(oldPwd, entity.getSalt()))){
			return Return.fail("原始密码错误");
		}
		return userService.changePwd(entity,pwd);
	}
	
	@ApiOperation(value="返回当前用户", notes="返回当前用户")
	@RequiresPermissions(value={"user:current"})
	@RequestMapping(value="/current",method=RequestMethod.GET)
	public User current(){
		//得到当前用户
		Subject currentUser = SecurityUtils.getSubject();
		Integer userId=(Integer) currentUser.getPrincipal();
		User entity = userService.findById(userId);
		entity.setPwd(null);
		entity.setSalt(null);
		return entity;
	}
	
	@Autowired
	private UserService userService;
	
}