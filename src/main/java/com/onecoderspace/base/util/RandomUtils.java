package com.onecoderspace.base.util;

import java.util.Random;

public class RandomUtils {

	private static final int[] numArr = {0,1,2,3,4,5,6,7,8,9};
	
	/**
	 * 随机获取两位数字
	 * @author yangwk
	 * @time 2017年7月28日 下午3:26:44
	 * @param length 数字长度
	 * @return
	 */
	public static String randomNum(int length){
		StringBuilder builder = new StringBuilder();
		Random random = new Random();
		for(int i=0;i<length;i++){
			builder.append(String.valueOf(numArr[random.nextInt(10)]));
		}
		return builder.toString();
	}
	

	public static int nextInt(int num) {
		Random random = new Random();
		return random.nextInt(num);
	}

	public static int nextInt(int start, int end) {
		Random random = new Random();
		int index = random.nextInt(end-start);
		return start+index;
	}
	
	public static void main(String[] args) {
		for(int i=0;i<20;i++){
			System.out.println(nextInt(3, 8));
		}
	}
}
