package com.poletto.bookstore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ObjectNotValidException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ObjectNotValidException(String msg) {
		super(msg);
	}

}
