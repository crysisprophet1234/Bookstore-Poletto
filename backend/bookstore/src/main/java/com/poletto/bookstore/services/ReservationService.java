package com.poletto.bookstore.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.dto.ReservationDTO;
import com.poletto.bookstore.entities.Reservation;
import com.poletto.bookstore.repositories.ReservationRepository;
import com.poletto.bookstore.services.exceptions.ResourceNotFoundException;

@Service
public class ReservationService {
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	@Transactional (readOnly = true)
	public List<ReservationDTO> findAll() {
		List<Reservation> list = reservationRepository.findAll();
		return list.stream().map(x -> new ReservationDTO(x)).toList();
	}
	
	@Transactional(readOnly = true)
	public ReservationDTO findById(Long id) {
		Optional<Reservation> user = reservationRepository.findById(id);
		Reservation entity = user.orElseThrow(() -> new ResourceNotFoundException(id)); 
		return new ReservationDTO(entity);	
	}
	
}
