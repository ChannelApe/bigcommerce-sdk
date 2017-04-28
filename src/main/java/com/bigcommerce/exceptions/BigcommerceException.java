package com.bigcommerce.exceptions;

public class BigcommerceException extends RuntimeException {

	private static final long serialVersionUID = 1238540756095356005L;

	public BigcommerceException(final String message, final Exception exception) {
		super(message, exception);
	}

}
