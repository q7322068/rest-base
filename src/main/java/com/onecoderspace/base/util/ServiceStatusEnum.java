package com.onecoderspace.base.util;

/**
 * 全局性状态码
 * @author yangwk
 */
public enum ServiceStatusEnum {
	UNLOGIN("0001"), //未登录
	ILLEGAL_TOKEN("0002"),//非法的token
	;
	public String code;
	
	private ServiceStatusEnum(String code){
		this.code = code;
	}
	
}
