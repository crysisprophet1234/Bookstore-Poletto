package com.poletto.bookstore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poletto.bookstore.entities.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	
	@Query(value = "SELECT * FROM tb_reservation res "
			 + "LEFT JOIN tb_book_reservation bookres "
			 + "ON res.id = bookres.reservation_id "
			 + "WHERE bookres.book_id = :id AND res.status NOT IN ('FINISHED')", nativeQuery = true)
	Reservation findByBook(Long id);
	
	@Query(value = "SELECT * from tb_reservation "
				 + "WHERE client_id = :id ", nativeQuery = true)
	Page<Reservation> findByClient(Long id, Pageable pageable);

}
