package com.ted.service;

public abstract class ServiceException extends Exception {

	protected ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException() {}

	private static final long serialVersionUID = 4213666133648941491L;
}
