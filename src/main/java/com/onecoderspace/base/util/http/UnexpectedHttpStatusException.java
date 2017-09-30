package com.onecoderspace.base.util.http;

public class UnexpectedHttpStatusException extends RuntimeException {

	private static final long serialVersionUID = -4664410630156843862L;

	public UnexpectedHttpStatusException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public UnexpectedHttpStatusException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnexpectedHttpStatusException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public UnexpectedHttpStatusException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public UnexpectedHttpStatusException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public UnexpectedHttpStatusException(int status) {
		super();
		this.status = status;
	}

	public UnexpectedHttpStatusException(int status, String message) {
		super(message);
		this.status = status;
	}

	private int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
