package com.ted.dao;

public class DBExUserExists extends DBException {

	private static final long serialVersionUID = -4791363879189523429L;
	final String message = "A user with the same username and/or email already exists";

	public DBExUserExists(Throwable cause) {
		super(cause);
	}

	@Override
	public String getMessage() {
		return message;
	}
}
