package com.ted.dao;

public class DBExJobExists extends DBException {

	private static final long serialVersionUID = -1995049570789077864L;
	final String message = "A job with the same name already exists for this project";

	public DBExJobExists(Throwable cause) {
		super(cause);
	}

	@Override
	public String getMessage() {
		return message;
	}
}
