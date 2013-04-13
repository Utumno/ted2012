package com.ted.service;

public class ServiceExDBFailure extends ServiceException {

	private static final long serialVersionUID = 2265838686771113250L;
	final String message = "An error occured in the Database";

	public ServiceExDBFailure(Throwable cause) {
		super(cause);
	}

	public ServiceExDBFailure() {}

	@Override
	public String getMessage() {
		return message;
	}
}
