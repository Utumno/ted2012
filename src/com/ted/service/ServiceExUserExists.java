package com.ted.service;

public class ServiceExUserExists extends ServiceException {

	private static final long serialVersionUID = 777351571307772567L;
	final String message = "A user with the same username and/or email already exists";

	public ServiceExUserExists(Throwable cause) {
		super(cause);
	}

	@Override
	public String getMessage() {
		return message;
	}
}
