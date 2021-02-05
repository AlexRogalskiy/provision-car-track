package com.provision.cartrack.helpers;

public class CreateResponse<T> extends SimpleResponse {

	private T payload;

	public CreateResponse(String message) {
		super(message);
	}

	public CreateResponse(String message, T payload) {
		super(message);
		this.payload = payload;
	}

	public T getPayload() {
		return payload;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}

}