package com.poletto.bookstore.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.dto.BookDTO;
import com.poletto.bookstore.dto.ReservationDTO;
import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.BookReservation;
import com.poletto.bookstore.entities.Reservation;
import com.poletto.bookstore.entities.enums.ReservationStatus;
import com.poletto.bookstore.repositories.BookRepository;
import com.poletto.bookstore.repositories.ReservationRepository;
import com.poletto.bookstore.repositories.UserRepository;
import com.poletto.bookstore.services.exceptions.ResourceNotFoundException;

@Service
public class ReservationService {
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
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
	
	@Transactional
	public ReservationDTO insert(ReservationDTO dto) {
		
		Reservation entity = new Reservation();
		
		copyDtoToEntity(dto, entity);
		
		entity = reservationRepository.save(entity);
		
		return new ReservationDTO(entity);
			
	}
	
	private void copyDtoToEntity(ReservationDTO dto, Reservation entity) {

		entity.setClient(userRepository.getReferenceById(dto.getClient().getId()));
		entity.setWeeks(dto.getWeeks());
		entity.setDevolution(entity.devolutionCalc(dto.getWeeks()));
		entity.setMoment(Instant.now());
		entity.setStatus(ReservationStatus.IN_PROGRESS);
				
		entity.getBooks().clear();
		for (BookDTO bookDTO : dto.getBooks()) {
			Book book = bookRepository.getReferenceById(bookDTO.getId());
			entity.getBooks().add(new BookReservation(null, entity, book));
			System.out.println(new BookReservation(null, entity, book));     //não está salvando na tabela associtiava tb_book_reservations
		}
	}
	
}
