package com.ted.service;

public class ServiceExProjectExists extends ServiceException {

	private static final long serialVersionUID = 4866306047777781466L;
	final String message = "A project with the same name already exists";

	public ServiceExProjectExists(Throwable cause) {
		super(cause);
	}

	@Override
	public String getMessage() {
		return message;
	}
}
