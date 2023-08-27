package com.poletto.bookstore.repositories.v1;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poletto.bookstore.entities.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	@Query("SELECT res FROM Reservation res " 
	     + "LEFT JOIN BookReservation bookres "
		 + "ON bookres.id.reservation.id = res.id "
		 + "WHERE bookres.id.book.id = :id AND res.status NOT IN ('FINISHED')")
	Reservation findByBook(Long id);

	@Query("SELECT res FROM Reservation res WHERE res.client.id = :id")
	Page<Reservation> findByClient(Long id, Pageable pageable);

}
