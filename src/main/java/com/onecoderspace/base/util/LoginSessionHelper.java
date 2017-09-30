package com.onecoderspace.base.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.request.RequestContextHolder;

public class LoginSessionHelper {

	/**
	 * 返回当前用户ID
	 * @return
	 */
	public static int getCurrentUserId(){
		Subject currentUser = SecurityUtils.getSubject();
		if(currentUser == null || currentUser.getPrincipal() == null){
			return 0;
		}
		Integer userId = (Integer) currentUser.getPrincipal();
		return userId;
	}
	
	/**
	 * 返回当前用户唯一标记，当用户已登录，返回用户ID，当用户未登录，返回sessionId
	 * @return
	 */
	public static String getCurrentUserKey(){
		Subject currentUser = SecurityUtils.getSubject();
		Integer userId = (Integer) currentUser.getPrincipal();
		if(userId == null){
			String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
			return sessionId;
		}
		return String.valueOf(userId);
	}
}
