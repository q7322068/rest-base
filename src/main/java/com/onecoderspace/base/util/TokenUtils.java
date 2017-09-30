package com.onecoderspace.base.util;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

/**
 * Token生成器
 */
public class TokenUtils {
	
	public static String defaultStr="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%";
	
	public static String defaultNum="123456789";

	/**
	 * 使用UUID生成指定长度的Token
	 * 
	 * @param length
	 *            长度:8位字符串
	 * @return
	 */
	public static String generateToken() {
		return UUID.randomUUID().toString().substring(0, 8);
	}
	
	/**
	 * 默认：随机一个指定长度的字符串符串
	 * @return
	 */
	public static String generateToken(int length){
		String str = defaultStr;
		Random rd = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
		   int number = rd.nextInt(str.length());
		   sb.append(str.charAt(number));
		}
		return sb.toString();
	}
	
	/**
	 * 默认：随机一个指定长度的字符串符串
	 * @return
	 */
	public static String generateToken(String str,int length){
		Random rd = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
		   int number = rd.nextInt(str.length());
		   sb.append(str.charAt(number));
		}
		return sb.toString();
	}


	public static void main(String[] args) throws IOException {
		System.out.println(generateToken(32));
		System.out.println(generateToken());
		System.out.println(generateToken(defaultNum,3));
	}

}
