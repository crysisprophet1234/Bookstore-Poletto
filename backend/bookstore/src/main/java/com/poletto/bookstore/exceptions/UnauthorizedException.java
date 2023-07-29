package com.poletto.bookstore.exceptions;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public UnauthorizedException(String msg) {
		super(msg);
	}

}
