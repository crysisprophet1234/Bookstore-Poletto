package com.poletto.bookstore.repositories.v3;

import java.time.Instant;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poletto.bookstore.entities.Reservation;

@Repository("ReservationRepositoryV3")
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	@Cacheable(value = "reservations")
	@Query("SELECT DISTINCT res "
		 + "FROM Reservation res "
		 + "INNER JOIN res.books books "
		 + "WHERE (:clientId IS NULL OR res.client.id = :clientId) "
		 + "AND (:bookId IS NULL OR books.id.book.id IN (:bookId)) "
		 + "AND (:status = 'ALL' OR res.status = :status) "
		 + "AND (cast(:startingDate as timestamp) IS NULL OR res.moment >= cast(:startingDate as timestamp)) "
		 + "AND (cast(:devolutionDate as timestamp) IS NULL OR res.devolution <= cast(:devolutionDate as timestamp))")
	Page<Reservation> findPaged(
			@Param("startingDate") Instant startingDate,
			@Param("devolutionDate") Instant devolutionDate,
			@Param("clientId") Long clientId,
			@Param("bookId") Long bookId,
			@Param("status") String status,
			Pageable pageable
	);
	
	@Cacheable(value = "reservation", key = "#id")
	Optional<Reservation> findById(Long id);

	@Caching(evict = { 
			  @CacheEvict(value = "reservations", allEntries = true),
			  @CacheEvict(value = "books", allEntries = true) ,
			  @CacheEvict(value = "reservation", key = "#entity.id") 
			})
	<S extends Reservation> S save(S entity);
	
}
