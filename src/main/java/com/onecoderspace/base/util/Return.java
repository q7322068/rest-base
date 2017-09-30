package com.onecoderspace.base.util;

import java.io.Serializable;

public class Return implements Serializable{

	private static final long serialVersionUID = -5515899356745165159L;

	private boolean success;
	
	private String msg;
	
	public Return(){
		
	}
	
	public Return(boolean success,String msg){
		this.success = success;
		this.msg = msg;
	}
	
	public static Return success(){
		return new Return(true,"");
	}
	
	public static Return fail(String msg){
		return new Return(false,msg);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "Return [success=" + success + ", msg=" + msg + "]";
	}
	
}
