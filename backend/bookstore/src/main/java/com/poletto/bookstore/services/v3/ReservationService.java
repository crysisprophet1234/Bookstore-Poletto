package com.poletto.bookstore.services.v3;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.controllers.v3.BookController;
import com.poletto.bookstore.controllers.v3.ReservationController;
import com.poletto.bookstore.controllers.v3.UserController;
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
import com.poletto.bookstore.repositories.v3.BookRepository;
import com.poletto.bookstore.repositories.v3.BookReservationRepository;
import com.poletto.bookstore.repositories.v3.ReservationRepository;
import com.poletto.bookstore.repositories.v3.UserRepository;
import com.poletto.bookstore.util.CustomRedisClient;

@Service("ReservationServiceV3")
public class ReservationService {

	private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

	@Autowired
	private CustomRedisClient<String, Object> redisClient;

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BookReservationRepository bookReservationRepository;

	@Autowired
	private BookRepository bookRepository;

	@Transactional(readOnly = true)
	public Page<ReservationDTOv2> findAllPaged(
			Pageable pageable,
			LocalDate startingDate,
			LocalDate devolutionDate,
			Long clientId,
			Long bookId,
			String status
		) {

		ZoneId zoneId = ZoneId.of("America/Sao_Paulo");

		Instant startDate = startingDate != null ? startingDate.atStartOfDay(zoneId).toInstant() : null;
		Instant endDate = devolutionDate != null ? devolutionDate.atStartOfDay(zoneId).toInstant() : null;

		Page<Reservation> entitiesPage = reservationRepository.findPaged(
				startDate,
				endDate,
				clientId,
				bookId,
				status.toString().toUpperCase(),
				pageable
		);

		logger.info("Resource RESERVATION page found: PAGE NUMBER [" + entitiesPage.getNumber() + "] "
				+ "- CONTENT: " + entitiesPage.getContent());

		Page<ReservationDTOv2> dtosPage = entitiesPage.map(x -> ReservationMapper.convertEntityToDtoV2(x));

		dtosPage.stream().forEach(dto -> {
			dto.add(linkTo(methodOn(ReservationController.class).findById(dto.getId())).withSelfRel().withType("GET"));
			dto.add(linkTo(methodOn(ReservationController.class).returnReservation(dto.getId())).withRel("return").withType("PUT"));
			dto.getBooks().forEach(book -> book.add(linkTo(methodOn(BookController.class).findById(book.getId())).withSelfRel().withType("GET")));
			dto.getClient().add(linkTo(methodOn(UserController.class).findById(dto.getClient().getId())).withSelfRel().withType("GET"));
		});

		return dtosPage;

	}

	@Transactional(readOnly = true)
	public ReservationDTOv2 findById(Long id) {

		Reservation entity = reservationRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Resource RESERVATION not found. ID " + id));

		logger.info("Resource RESERVATION found: " + entity.toString());

		ReservationDTOv2 dto = ReservationMapper.convertEntityToDtoV2(entity);

		dto.add(linkTo(methodOn(ReservationController.class).findById(dto.getId())).withSelfRel().withType("GET"));
		dto.add(linkTo(methodOn(ReservationController.class).returnReservation(dto.getId())).withRel("return").withType("PUT"));
		dto.getBooks().forEach(book -> book.add(linkTo(methodOn(BookController.class).findById(book.getId())).withSelfRel().withType("GET")));
		dto.getClient().add(linkTo(methodOn(UserController.class).findById(dto.getClient().getId())).withSelfRel().withType("GET"));
		
		return dto;
		
	}

	@Transactional
	public ReservationDTOv2 reserveBooks(ReservationDTOv2 dto) {

		Reservation entity = new Reservation();

		entity.setStatus(ReservationStatus.IN_PROGRESS);

		entity.setClient(userRepository.findById(dto.getClient().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Resource USER not found. ID " + dto.getClient().getId())));

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
			
			if (redisClient.put("book::" + book.getBook().getId(), book.getBook())) {
				logger.info("Cache book::{} status changed to BOOKED", book.getBook().getId());
			}
			
		}

		logger.info("Resource RESERVATION saved: " + entity.toString());

		logger.info("Resource BOOK status changed: " + entity.getBooks());

		ReservationDTOv2 newDto = ReservationMapper.convertEntityToDtoV2(entity);

		newDto.add(linkTo(methodOn(ReservationController.class).findById(newDto.getId())).withSelfRel().withType("GET"));
		newDto.add(linkTo(methodOn(ReservationController.class).returnReservation(newDto.getId())).withRel("return").withType("PUT"));
		newDto.getBooks().forEach(book -> book.add(linkTo(methodOn(BookController.class).findById(book.getId())).withSelfRel().withType("GET")));
		newDto.getClient().add(linkTo(methodOn(UserController.class).findById(newDto.getClient().getId())).withSelfRel().withType("GET"));

		return newDto;

	}
	
	@Transactional
	public void returnReservation(Long reservationId) {

		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource RESERVATION not found. ID " + reservationId));

		if (reservation.getStatus().equals(ReservationStatus.FINISHED)) throw new InvalidStatusException(reservation);
		
		for (BookReservation bookRes : reservation.getBooks()) {
			
			bookRes.getBook().setStatus(BookStatus.AVAILABLE);
			
			bookRepository.save(bookRes.getBook());
			
			if (redisClient.put("book::" + bookRes.getBook().getId(), bookRes.getBook())) {

				logger.info("Cache book::{} status changed to AVAILABLE", bookRes.getBook().getId());

			}
			
		}
		
		reservation.setStatus(ReservationStatus.FINISHED);

		reservationRepository.save(reservation);

		logger.info("Resource RESERVATION status changed to FINISHED: {}", reservation);

	}

}