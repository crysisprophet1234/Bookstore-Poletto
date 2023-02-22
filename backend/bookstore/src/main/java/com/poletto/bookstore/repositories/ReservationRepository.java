package com.poletto.bookstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poletto.bookstore.entities.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
