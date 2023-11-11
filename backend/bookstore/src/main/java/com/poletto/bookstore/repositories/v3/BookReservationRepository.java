package com.poletto.bookstore.repositories.v3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poletto.bookstore.entities.BookReservation;

@Repository("BookReservationRepositoryV3")
public interface BookReservationRepository extends JpaRepository<BookReservation, Long> {

}
