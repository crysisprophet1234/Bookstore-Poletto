package com.poletto.bookstore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poletto.bookstore.dto.ReservationDTO;
import com.poletto.bookstore.services.ReservationService;

@RestController
@RequestMapping (value = "/api/v1/reservations")
public class ReservationController {
	
	@Autowired
	private ReservationService reservationService;
	
	@GetMapping
	public ResponseEntity<List<ReservationDTO>> findAll() {
		List<ReservationDTO> reservations = reservationService.findAll();
		return ResponseEntity.ok().body(reservations);
	}
	
	@GetMapping (value = "/{id}")
	public ResponseEntity<ReservationDTO> findById(@PathVariable Long id) {
		ReservationDTO reservation = reservationService.findById(id);
		return ResponseEntity.ok().body(reservation);
	}

}
