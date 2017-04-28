package com.bigcommerce.exceptions;

import javax.ws.rs.core.Response;

public class BigcommerceErrorResponseException extends RuntimeException {

	static final String MESSAGE = "Received unexpected Response Status Code of %d and Body of:\n%s";

	private static final long serialVersionUID = 5646635633348617058L;

	public BigcommerceErrorResponseException(final Response response) {
		super(buildMessage(response));

	}

	private static String buildMessage(final Response response) {
		response.bufferEntity();
		return String.format(MESSAGE, response.getStatus(), response.readEntity(String.class));
	}

}
