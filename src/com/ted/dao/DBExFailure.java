package com.ted.dao;

public class DBExFailure extends DBException {

	private static final long serialVersionUID = -7978924518840893498L;
	final String message = "An error occured in the Database";

	public DBExFailure(Throwable cause) {
		super(cause);
	}

	public DBExFailure() {}

	@Override
	public String getMessage() {
		return message;
	}
}
