package com.poletto.bookstore.services.exceptions;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.Reservation;

@ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE, reason="Invalid status change")
public class InvalidStatus extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public InvalidStatus(Book book) {
		super("Invalid status for book ID " + book.getId());
	}
	
	public InvalidStatus(Reservation reservation) {
		super("Invalid status for reservation ID " + reservation.getId());
	}

}
