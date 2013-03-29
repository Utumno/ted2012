package com.ted.dao;

public class DBExProjectExists extends DBException {

	private static final long serialVersionUID = -3886586196294377318L;
	final String message = "A project with the same name already exists";

	public DBExProjectExists(Throwable cause) {
		super(cause);
	}

	@Override
	public String getMessage() {
		return message;
	}
}
