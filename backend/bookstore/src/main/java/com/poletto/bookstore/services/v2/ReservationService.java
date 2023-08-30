package com.poletto.bookstore.services.v2;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
import com.poletto.bookstore.controllers.v2.UserController;
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
import com.poletto.bookstore.repositories.v2.BookRepository;
import com.poletto.bookstore.repositories.v2.BookReservationRepository;
import com.poletto.bookstore.repositories.v2.ReservationRepository;
import com.poletto.bookstore.repositories.v2.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service("ReservationServiceV2")
public class ReservationService {

	private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private BookReservationRepository bookReservationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	@Deprecated
	@Transactional(readOnly = true)
	public Page<ReservationDTOv2> findAll(Pageable pageable, Long userId) {

		Page<Reservation> reservationPage = reservationRepository.findAll(pageable);

		logger.info("Resource RESERVATION page found: " + "PAGE NUMBER [" + reservationPage.getNumber()
				+ "] - CONTENT: " + reservationPage.getContent());

		Page<ReservationDTOv2> dtos = reservationPage.map(x -> ReservationMapper.convertEntityToDtoV2(x));

		dtos.stream().forEach(dto -> {
			dto.add(linkTo(methodOn(ReservationController.class).findById(dto.getId())).withSelfRel().withType("GET"))
					.add(linkTo(methodOn(ReservationController.class).returnReservation(dto.getId())).withRel("return")
							.withType("PUT"));
			dto.getBooks().forEach(book -> book
					.add(linkTo(methodOn(BookController.class).findById(book.getId())).withSelfRel().withType("GET")));
			dto.getClient().add(linkTo(methodOn(UserController.class).findById(dto.getClient().getId())).withSelfRel()
					.withType("GET"));
		});

		return dtos;

	}
	
	@Transactional(readOnly = true)
	public Page<ReservationDTOv2> findAllPaged(Pageable pageable, LocalDate startingDate, LocalDate devolutionDate, Long clientId, Long bookId, String status) {
		
		ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
		
		Instant startDate = startingDate != null ? startingDate.atStartOfDay(zoneId).toInstant() : null;
		Instant endDate = devolutionDate != null ? devolutionDate.atStartOfDay(zoneId).toInstant() : null;
		
		Page<Reservation> reservationPage = reservationRepository.findPaged(
				startDate,
				endDate,
				clientId,
				bookId,
				status.toString().toUpperCase(),
				pageable);
		
		logger.info("Resource RESERVATION page found: PAGE NUMBER [" + reservationPage.getNumber() + "] "
				  + "- CONTENT: " + reservationPage.getContent());
		
		Page<ReservationDTOv2> dtos = reservationPage.map(x -> ReservationMapper.convertEntityToDtoV2(x));
		
		dtos.stream().forEach(dto -> {
			dto.add(linkTo(methodOn(ReservationController.class).findById(dto.getId())).withSelfRel().withType("GET"))
					.add(linkTo(methodOn(ReservationController.class).returnReservation(dto.getId())).withRel("return").withType("PUT"));
			dto.getBooks().forEach(book -> book
					.add(linkTo(methodOn(BookController.class).findById(book.getId())).withSelfRel().withType("GET")));
			dto.getClient().add(linkTo(methodOn(UserController.class).findById(dto.getClient().getId())).withSelfRel().withType("GET"));
		});

		return dtos;
		
	}

	@Transactional(readOnly = true)
	public ReservationDTOv2 findById(Long id) {

		Optional<Reservation> reservation = reservationRepository.findById(id);

		var entity = reservation
				.orElseThrow(() -> new ResourceNotFoundException("Resource RESERVATION not found. ID " + id));

		logger.info("Resource RESERVATION found: " + entity.toString());

		ReservationDTOv2 dto = ReservationMapper.convertEntityToDtoV2(entity);

		dto.add(linkTo(methodOn(ReservationController.class).findById(dto.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(ReservationController.class).returnReservation(dto.getId())).withRel("return")
						.withType("PUT"));
		dto.getBooks().forEach(book -> book
				.add(linkTo(methodOn(BookController.class).findById(book.getId())).withSelfRel().withType("GET")));
		dto.getClient().add(
				linkTo(methodOn(UserController.class).findById(dto.getClient().getId())).withSelfRel().withType("GET"));

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

			Book bookEntity = bookRepository.findById(bookDTO.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Resource BOOK not found. ID " + bookDTO.getId()));

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

		newDto.add(linkTo(methodOn(ReservationController.class).findById(newDto.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(ReservationController.class).returnReservation(newDto.getId())).withRel("return")
						.withType("PUT"));
		newDto.getBooks().forEach(book -> book
				.add(linkTo(methodOn(BookController.class).findById(book.getId())).withSelfRel().withType("GET")));
		newDto.getClient().add(linkTo(methodOn(UserController.class).findById(newDto.getClient().getId())).withSelfRel()
				.withType("GET"));

		return newDto;

	}

	public void returnReservation(Long reservationId) {

		try {

			var reservation = reservationRepository.getReferenceById(reservationId);

			if (reservation.getStatus().equals(ReservationStatus.IN_PROGRESS)) {

				reservation.getBooks().forEach(x -> returnBook(x.getBook()));

				reservation.setStatus(ReservationStatus.FINISHED);

			} else {

				throw new InvalidStatusException(reservation);
			}

			reservationRepository.save(reservation);

			logger.info("Resource RESERVATION status changed to FINISHED: " + reservation);
			logger.info("Resource BOOK status changed to AVAILABLE: " + reservation.getBooks());

		} catch (EntityNotFoundException ex) {

			throw new ResourceNotFoundException("Resource RESERVATION id " + reservationId + " not found.");

		}

	}

	public void returnBook(Book book) {
		book.setStatus(BookStatus.AVAILABLE);
		bookRepository.save(book);
	}

}
