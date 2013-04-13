package com.ted.dao;

public abstract class DBException extends Exception {

	private static final long serialVersionUID = 9209049250058865231L;

	public DBException() {}

	public DBException(Throwable cause) {
		super(cause);
	}
}
