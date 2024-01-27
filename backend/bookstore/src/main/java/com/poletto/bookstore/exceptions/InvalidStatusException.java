package com.poletto.bookstore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.Reservation;
import com.poletto.bookstore.entities.User;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidStatusException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidStatusException(Book book) {
		super("Status inválido para o livro ID " + book.getId() + ", permanecerá o status " + book.getStatus());
	}

	public InvalidStatusException(Reservation reservation) {
		super("Invalid status for reservation ID " + reservation.getId());
	}
	
	public InvalidStatusException(User user) {
		super("Status inválido para o usuário ID " + user.getId() + ", permanecerá o status " + user.getUserStatus());
	}
	
	public InvalidStatusException(Long userId) {
		super("Usuário ID " + userId + " já está verificado");
	}
}
