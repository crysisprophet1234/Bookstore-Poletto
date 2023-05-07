package com.poletto.bookstore.controllers;

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

import com.poletto.bookstore.dto.v1.ReservationDTO;
import com.poletto.bookstore.services.ReservationService;

@RestController
@RequestMapping(value = "/reservations/v1")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;

	@GetMapping
	public ResponseEntity<Page<ReservationDTO>> findAll(@RequestParam (value = "client", defaultValue = "0") Long userId, Pageable pageable) {
		Page<ReservationDTO> reservations = reservationService.findAll(pageable, userId);
		return ResponseEntity.ok().body(reservations);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<ReservationDTO> findById(@PathVariable Long id) {
		ReservationDTO reservation = reservationService.findById(id);
		return ResponseEntity.ok().body(reservation);
	}

	@PostMapping
	public ResponseEntity<ReservationDTO> insert(@RequestBody ReservationDTO dto) {
		dto = reservationService.reserveBooks(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PutMapping(value = "/return/{id}")
	public ResponseEntity<ReservationDTO> updateStatus(@PathVariable Long id) {
		reservationService.returnBooks(id);
		return ResponseEntity.noContent().build();
	}

}
