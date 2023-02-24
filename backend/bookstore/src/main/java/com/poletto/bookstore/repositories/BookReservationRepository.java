package com.poletto.bookstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poletto.bookstore.entities.BookReservation;

public interface BookReservationRepository extends JpaRepository<BookReservation, Long> {

}
