package com.ted.service;

public class ServiceExUserIsProjectManager extends ServiceException {

	private static final long serialVersionUID = -6796391516929059063L;
	final String message = "You cannot change the role of this user";

	@Override
	public String getMessage() {
		return message;
	}
}
