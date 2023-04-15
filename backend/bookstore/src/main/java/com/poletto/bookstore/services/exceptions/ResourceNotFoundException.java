package com.poletto.bookstore.services.exceptions;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such resource")
public class ResourceNotFoundException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(ResourceNotFoundException.class);

	public ResourceNotFoundException(Object id, String entity) {
		super("Resource " + entity.toUpperCase() + " not found. ID: " + id);
		logger.warn("Resource " + entity.toUpperCase() + " not found. ID: " + id);
	}

}
