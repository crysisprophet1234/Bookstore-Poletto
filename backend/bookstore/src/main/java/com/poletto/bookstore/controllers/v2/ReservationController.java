package com.poletto.bookstore.controllers.v2;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

@RestController("ReservationControllerV2")
@RequestMapping(value = "/reservations/v2")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;

	@GetMapping
	public ResponseEntity<Page<ReservationDTOv2>> findAll(@RequestParam (value = "client", defaultValue = "0") Long userId, Pageable pageable) {
		Page<ReservationDTOv2> reservations = reservationService.findAll(pageable, userId);
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
