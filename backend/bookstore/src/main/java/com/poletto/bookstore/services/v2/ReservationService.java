package com.poletto.bookstore.services.v2;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.controllers.v2.BookController;
import com.poletto.bookstore.controllers.v2.ReservationController;
import com.poletto.bookstore.converter.custom.ReservationMapper;
import com.poletto.bookstore.dto.v2.BookDTOv2;
import com.poletto.bookstore.dto.v2.ReservationDTOv2;
import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.BookReservation;
import com.poletto.bookstore.entities.Reservation;
import com.poletto.bookstore.entities.enums.BookStatus;
import com.poletto.bookstore.entities.enums.ReservationStatus;
import com.poletto.bookstore.exceptions.InvalidStatusException;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.repositories.BookRepository;
import com.poletto.bookstore.repositories.BookReservationRepository;
import com.poletto.bookstore.repositories.ReservationRepository;
import com.poletto.bookstore.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service("ReservationServiceV2")
public class ReservationService {

	// TODO testar reservation
	// for some reason its not saving more than one book ...

	private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private BookReservationRepository bookReservationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	@Transactional(readOnly = true)
	public Page<ReservationDTOv2> findAll(Pageable pageable, Long userId) {

		Page<Reservation> reservationPage = reservationRepository.findAll(pageable);

		if (userId > 0) {
			reservationPage = reservationRepository.findByClient(userId, pageable);
		}

		logger.info("Resource RESERVATION page found: " + "PAGE NUMBER [" + reservationPage.getNumber()
				+ "] - CONTENT: " + reservationPage.getContent());

		Page<ReservationDTOv2> dtos = reservationPage.map(x -> ReservationMapper.convertEntityToDtoV2(x));

		dtos.stream().forEach(x -> x
				.add(linkTo(methodOn(ReservationController.class).findById(x.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(ReservationController.class).updateStatus(x.getId())).withRel("return").withType("PUT")));

		return dtos;

	}

	@Transactional(readOnly = true)
	public ReservationDTOv2 findById(Long id) {

		Optional<Reservation> user = reservationRepository.findById(id);

		var entity = user.orElseThrow(() -> new ResourceNotFoundException("Resource RESERVATION not found. ID " + id));

		logger.info("Resource RESERVATION found: " + entity.toString());
		
		ReservationDTOv2 dto = ReservationMapper.convertEntityToDtoV2(entity);
		
		for (BookDTOv2 bookDTO : dto.getBooks()) {
			
			bookDTO.add(linkTo(methodOn(BookController.class).findById(bookDTO.getId())).withSelfRel().withType("GET"));
			bookDTO.add(linkTo(methodOn(BookController.class).delete(bookDTO.getId())).withRel("delete").withType("DELETE"));
			bookDTO.add(linkTo(methodOn(BookController.class).update(bookDTO.getId(), bookDTO)).withRel("update").withType("PUT"));
			
		}
		
		dto.add(linkTo(methodOn(ReservationController.class).findById(dto.getId())).withSelfRel().withType("GET"));
		dto.add(linkTo(methodOn(ReservationController.class).updateStatus(dto.getId())).withRel("return").withType("PUT"));

		return dto;

	}

	@Transactional
	public ReservationDTOv2 reserveBooks(ReservationDTOv2 dto) {

		Reservation entity = new Reservation();

		entity.setStatus(ReservationStatus.IN_PROGRESS);

		entity.setClient(userRepository.getReferenceById(dto.getClient().getId()));

		entity.setWeeks(dto.getWeeks());

		entity.setMoment(Instant.now());

		entity.setDevolution(entity.devolutionCalc(dto.getWeeks()));

		entity.getBooks().clear();

		for (BookDTOv2 bookDTO : dto.getBooks()) {

			Book bookEntity = bookRepository.getReferenceById(bookDTO.getId());

			if (bookEntity.getStatus().equals(BookStatus.BOOKED)) {
				throw new InvalidStatusException(bookEntity);
			}

			bookEntity.setStatus(BookStatus.BOOKED);
			entity.getBooks().add(new BookReservation(entity, bookEntity));
			
		}

		entity = reservationRepository.save(entity);

		for (BookReservation book : entity.getBooks()) {
			bookReservationRepository.save(book);
		}

		logger.info("Resource RESERVATION saved: " + entity.toString());

		logger.info("Resource BOOK status changed: " + entity.getBooks());
		
		ReservationDTOv2 newDto = ReservationMapper.convertEntityToDtoV2(entity);
		
		for (BookDTOv2 bookDTO : newDto.getBooks()) {
			
			bookDTO.add(linkTo(methodOn(BookController.class).findById(bookDTO.getId())).withSelfRel().withType("GET"));
			bookDTO.add(linkTo(methodOn(BookController.class).delete(bookDTO.getId())).withRel("delete").withType("DELETE"));
			bookDTO.add(linkTo(methodOn(BookController.class).update(bookDTO.getId(), bookDTO)).withRel("update").withType("PUT"));
			
		}
		
		newDto.add(linkTo(methodOn(ReservationController.class).findById(newDto.getId())).withSelfRel().withType("GET"));
		newDto.add(linkTo(methodOn(ReservationController.class).updateStatus(newDto.getId())).withRel("return").withType("PUT"));

		return newDto;

	}

	// TODO change this logic when frontend is capable to reserve more than one book
	
	// TODO should it really return by book id???

	@Transactional
	public void returnBooks(Long bookId) {

		try {

			Book book = bookRepository.getReferenceById(bookId);

			if (book.getStatus().equals(BookStatus.AVAILABLE)) {
				throw new InvalidStatusException(book);
			}

			Reservation entity = reservationRepository.findByBook(bookId);

			if (entity.getStatus().equals(ReservationStatus.FINISHED)) {
				throw new InvalidStatusException(entity);
			}

			for (BookReservation bookReservation : entity.getBooks()) {
				Book bookEntity = bookRepository.getReferenceById(bookReservation.getBook().getId());
				if (bookEntity.getStatus().equals(BookStatus.AVAILABLE)) {
					throw new InvalidStatusException(bookEntity);
				}
				bookEntity.setStatus(BookStatus.AVAILABLE);
				bookRepository.save(bookEntity);
			}

			entity.setStatus(ReservationStatus.FINISHED);

			reservationRepository.save(entity);

			logger.info("Resource RESERVATION status changed to FINISHED: " + entity);
			logger.info("Resource BOOK status changed to AVAILABLE: " + entity.getBooks());

		} catch (EntityNotFoundException e) {

			throw new ResourceNotFoundException("Resource RESERVATION not found for book ID " + bookId);

		}

	}

}
