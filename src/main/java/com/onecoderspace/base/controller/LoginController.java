package com.onecoderspace.base.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.onecoderspace.base.domain.User;
import com.onecoderspace.base.service.UserService;
import com.onecoderspace.base.util.SaltMD5Util;

@Api(value="用户登录",tags={"用户登录"})
@RestController
public class LoginController {
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Value("${server.session.timeout}")
	private String serverSessionTimeout;

	/**
	 * 用户登录接口 通过用户名和密码进行登录
	 */
	@ApiOperation(value = "用户登录接口 通过用户名和密码进行登录", notes = "用户登录接口 通过用户名和密码进行登录")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "username", value = "用户名", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "pwd", value = "密码", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "autoLogin", value = "自动登录", required = true, dataType = "boolean")})
	@RequestMapping(value = "/login/submit",method={RequestMethod.GET,RequestMethod.POST})
	public Map<String, String> subm(HttpServletRequest request,HttpServletResponse response,
			String username,String pwd,@RequestParam(value = "autoLogin", defaultValue = "false") boolean autoLogin) {
		Map<String, String> map = Maps.newLinkedHashMap();
		Subject currentUser = SecurityUtils.getSubject();
		User user = userService.findByUsername(username);
		if (user == null) {
			map.put("code", "-1");
			map.put("description", "账号不存在");
			return map;
		}
		if (user.getEnable() == 0) { //账号被禁用
			map.put("code", "-1");
			map.put("description", "账号已被禁用");
			return map;
		}

		String salt = user.getSalt();
		UsernamePasswordToken token = null;
		Integer userId = user.getId();
		token = new UsernamePasswordToken(userId.toString(),SaltMD5Util.encode(pwd, salt));
		token.setRememberMe(autoLogin);

		loginValid(map, currentUser, token);

		// 验证是否登录成功
		if (currentUser.isAuthenticated()) {
			map.put("code","1");
			map.put("description", "ok");
			map.put("id", String.valueOf(userId));
			map.put("username", user.getUsername());
			map.put("name", user.getName());
			String uuidToken = UUID.randomUUID().toString();
			map.put("token", uuidToken);
			
			currentUser.getSession().setTimeout(NumberUtils.toLong(serverSessionTimeout, 1800)*1000);
			request.getSession().setAttribute("token",uuidToken );
		} else {
			map.put("code", "-1");
			token.clear();
		}
		return map;
	}
	
	@RequestMapping(value="test/set",method=RequestMethod.GET)
    public String testSet(HttpServletRequest request) {
		request.getSession().setAttribute("test", "test");
        return "success";
    }
	
	@RequestMapping(value="test/get",method=RequestMethod.GET)
    public String testGet(HttpServletRequest request) {
		String value = (String) request.getSession().getAttribute("test");
        return value == null ? "null" : value;
    }
	
	@RequestMapping(value="logout",method=RequestMethod.GET)
    public Map<String, String> logout() {
        Map<String, String> map = Maps.newLinkedHashMap();
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        map.put("code", "logout");
        return map;
    }
	
	@RequestMapping(value="unauth",method=RequestMethod.GET)
    public Map<String, String> unauth() {
        Map<String, String> map = Maps.newLinkedHashMap();
        map.put("code", "403");
        map.put("msg", "你没有访问权限");
        return map;
    }

	private boolean loginValid(Map<String, String> map,Subject currentUser, UsernamePasswordToken token) {
		String username = null;
		if (token != null) {
			username = (String) token.getPrincipal();
		}

		try {
			// 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
			// 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
			// 所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
			currentUser.login(token);
			return true;
		} catch (UnknownAccountException | IncorrectCredentialsException ex) {
			map.put("description", "账号或密码错误");
		} catch (LockedAccountException lae) {
			map.put("description","账户已锁定");
		} catch (ExcessiveAttemptsException eae) {
			map.put("description", "错误次数过多");
		} catch (AuthenticationException ae) {
			// 通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
			map.put("description", "登录失败");
			logger.warn(String.format("对用户[%s]进行登录验证..验证未通过", username),ae);
		}
		return false;
	}
	
	@Autowired
	private UserService userService;
}
