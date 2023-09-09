package com.poletto.bookstore.repositories.v2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poletto.bookstore.entities.BookReservation;

@Repository("BookReservationRepositoryV2")
public interface BookReservationRepository extends JpaRepository<BookReservation, Long> {

}
