package com.poletto.bookstore.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "book_reservation")
public class BookReservation implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BookReservationPK id = new BookReservationPK();

	public BookReservation() {

	}

	public BookReservation(Reservation reservation, Book book) {
		super();
		id.setReservation(reservation);
		id.setBook(book);
	}

	public Reservation getReservation () {
		return id.getReservation();
	}
	
	public void setReservation (Reservation reservation) {
		id.setReservation(reservation);
	}
	
	public Book getBook () {
		return id.getBook();
	}
	
	public void setBook (Book book) {
		id.setBook(book);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookReservation other = (BookReservation) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "BookReservation [id=" + id + "]";
	}
	
}
