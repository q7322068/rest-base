package com.onecoderspace.base.util;

public class OperationCheck {

	/**
	 * 校验数据拥有者是否是当前用户
	 * @author yangwk
	 * @time 2017年7月27日 上午9:52:05
	 * @param uid 数据拥有者ID
	 * @return
	 */
	public static boolean isOwnerCurrentUser(int uid){
		if(uid == LoginSessionHelper.getCurrentUserId()){
			return true;
		}
		return false;
	}
	
}
