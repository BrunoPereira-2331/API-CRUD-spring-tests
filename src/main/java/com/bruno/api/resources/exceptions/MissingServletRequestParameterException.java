package com.bruno.api.resources.exceptions;

public class MissingServletRequestParameterException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public MissingServletRequestParameterException(String msg) {
		super(msg);
	}

	public MissingServletRequestParameterException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
