package com.poletto.bookstore.services.exceptions;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such resource")
public class ResourceNotFoundException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(Object id) {
		super("Resource not found. ID: " + id);
	}

}
