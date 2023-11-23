package com.poletto.bookstore.controllers.v2;

import java.net.URI;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poletto.bookstore.dto.v2.ReservationDTOv2;
import com.poletto.bookstore.services.v2.ReservationService;

import io.swagger.v3.oas.annotations.Hidden;

@RestController("ReservationControllerV2")
@RequestMapping(value = "/reservations/v2")
@Hidden
public class ReservationController {

	@Autowired
	private ReservationService reservationService;

	@GetMapping
	public ResponseEntity<Page<ReservationDTOv2>> findAllPaged(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "5") Integer size,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "sort", defaultValue = "asc") String sort,
			@RequestParam(value = "startingDate", defaultValue = "") LocalDate startingDate,
			@RequestParam(value = "devolutionDate", defaultValue = "") LocalDate devolutionDate,
			@RequestParam(value = "client", defaultValue = "") Long clientId,
			@RequestParam(value = "book", defaultValue = "") Long bookId,
			@RequestParam(value = "status", defaultValue = "all") String status) {
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.valueOf(sort.toUpperCase()), orderBy));
		
		Page<ReservationDTOv2> reservations = reservationService.findAllPaged(pageable, startingDate, devolutionDate, clientId, bookId, status);
		
		return ResponseEntity.ok().body(reservations);
		
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<ReservationDTOv2> findById(@PathVariable Long id) {
		ReservationDTOv2 reservation = reservationService.findById(id);
		return ResponseEntity.ok().body(reservation);
	}

	@PostMapping
	public ResponseEntity<ReservationDTOv2> insert(@RequestBody ReservationDTOv2 dto) {
		dto = reservationService.reserveBooks(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@PutMapping(value = "/return/{id}")
	public ResponseEntity<ReservationDTOv2> returnReservation(@PathVariable Long id) {
		reservationService.returnReservation(id);
		return ResponseEntity.noContent().build();
	}

}
