package com.onecoderspace.base.util;

import org.apache.commons.lang3.StringUtils;


/**
 * 加盐MD5算法
 * 用户账号信息内保存salt，和md5(compone(salt,pwd)),用户登录时用同样的算法加密后进行匹配
 * 这样即使用户数据泄露，攻击者也极难破解用户信息
 * @author yangwenkui
 * @version v2.0
 */
public class SaltMD5Util {

	/**
	 * 获取MD5加密参数
	 * @author yangwenkui
	 * @time 2017年4月25日 下午4:53:57
	 * @param length length值 建议16位或32位
	 * @return
	 */
	public static String getSalt(){
		return TokenUtils.generateToken(16);
	}
	
	/**
	 * 通过salt+MD5方式加密
	 * @author yangwenkui
	 * @time 2017年4月25日 下午5:20:32
	 * @param pwd
	 * @param salt
	 * @return
	 */
	public static String encode(String pwd,String salt){
		if(StringUtils.isBlank(salt) || salt.length() != 16){
			salt = TokenUtils.generateToken(16);
		}
		String mixResult = mixStr(pwd,salt);
		return MD5.encodeMd5(mixResult);
	}

	/**
	 * 将密码明文和salt混合，混合方式为salt一个字符+一个密码明文字符，逐个追加
	 * 如：密码明文（123），salt（abcd），则结果为a1b2c3d
	 * @author yangwenkui
	 * @time 2017年4月25日 下午5:12:02
	 * @param pwd 密码明文
	 * @param salt 随机获取的加密字符 16位
	 * @return
	 */
	private static String mixStr(String pwd, String salt) {
		StringBuilder builder = new StringBuilder();
		builder.append(salt.substring(0, 5));
		String saltLast = salt.substring(5);
		for(int i=0; i<pwd.length(); i++){
			if(saltLast.length() > i){
				builder.append(saltLast.charAt(i));
			}
			builder.append(pwd.charAt(i));
		}
		if(saltLast.length() > pwd.length()){
			builder.append(saltLast.substring(pwd.length()));
		}
		return builder.toString();
	}
	
	public static void main(String[] args) {
		String salt = "HpsuQ2d4FkqjGU01";//getSalt();
		System.err.println(salt);
		System.err.println(encode("eva2017admin", salt));
	}
	
}

