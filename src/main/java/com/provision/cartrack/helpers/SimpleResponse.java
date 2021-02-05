package com.provision.cartrack.helpers;

public class SimpleResponse {

	private String message;

	public SimpleResponse(String message) {
		super();
		this.message = message;
	}

	public SimpleResponse() {
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}