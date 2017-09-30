package com.onecoderspace.base.util;

import java.security.MessageDigest;



/**MD5加密算法
 * @author songsp 2010-1-13
 *
 */
public class MD5 {
	public final static String encodeMd5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");//返回实现MD5摘要算法的 MessageDigest 对象
			mdTemp.update(strTemp);//使用指定的字节更新摘要
			byte[] md = mdTemp.digest();// 得到md5算法结果 //通过执行诸如填充之类的最终操作完成哈希计算
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];// 分成高低4位处理
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	
	
	public static void main(String[] args) {
		String key = System.currentTimeMillis()+"";
		System.out.println(key);
		System.out.println(MD5.encodeMd5("md5cenwavepublickey"+key+"cenwave1000001"));
		System.out.println(MD5.encodeMd5("bosskey2010-05-30 16:00:002010-06-08 15:59:59"));
		
	}
}
