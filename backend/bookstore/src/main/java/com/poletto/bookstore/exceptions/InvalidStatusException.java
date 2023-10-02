package com.poletto.bookstore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.Reservation;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidStatusException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidStatusException(Book book) {
		super("Invalid status for book ID " + book.getId());
	}

	public InvalidStatusException(Reservation reservation) {
		super("Invalid status for reservation ID " + reservation.getId());
	}

}
