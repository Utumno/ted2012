package com.ted.service;

public class ServiceExJobExists extends ServiceException {

	private static final long serialVersionUID = -5523562663388111023L;
	final String message = "A job with the same name already exists for this project";

	public ServiceExJobExists(Throwable cause) {
		super(cause);
	}

	@Override
	public String getMessage() {
		return message;
	}
}
