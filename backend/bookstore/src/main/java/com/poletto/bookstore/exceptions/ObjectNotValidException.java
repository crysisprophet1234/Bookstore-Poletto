package com.poletto.bookstore.exceptions;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ObjectNotValidException extends RuntimeException implements Serializable  {

	private static final long serialVersionUID = 1L;

	public ObjectNotValidException(String msg) {
		super(msg);
	}

}
