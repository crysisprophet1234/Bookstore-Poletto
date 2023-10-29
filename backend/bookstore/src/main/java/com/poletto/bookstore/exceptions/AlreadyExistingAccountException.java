package com.poletto.bookstore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class AlreadyExistingAccountException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public AlreadyExistingAccountException(String email) {
		super("Email provided is already registered");
	}

}
