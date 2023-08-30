package com.poletto.bookstore.repositories.v1;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poletto.bookstore.entities.BookReservation;

public interface BookReservationRepository extends JpaRepository<BookReservation, Long> {

}
