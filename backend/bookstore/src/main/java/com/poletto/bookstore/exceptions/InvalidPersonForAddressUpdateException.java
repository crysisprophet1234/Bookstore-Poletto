package com.poletto.bookstore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidPersonForAddressUpdateException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InvalidPersonForAddressUpdateException(Long addressId, Long personId) {
		super("Address ID provided doesnt belong to the person with ID " + personId);
	}

}