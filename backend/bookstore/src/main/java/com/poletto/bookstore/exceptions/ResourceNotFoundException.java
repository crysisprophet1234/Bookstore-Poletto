package com.poletto.bookstore.exceptions;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public ResourceNotFoundException(String msg) {
		super(msg);
	}

}