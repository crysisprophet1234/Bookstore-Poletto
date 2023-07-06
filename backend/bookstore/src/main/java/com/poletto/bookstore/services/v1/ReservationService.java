package com.poletto.bookstore.services.v1;

import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.converter.custom.ReservationMapper;
import com.poletto.bookstore.dto.v1.ReservationDTOv1;
import com.poletto.bookstore.dto.v1.BookDTOv1;
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

@Service
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

	@Transactional(readOnly = true)
	public Page<ReservationDTOv1> findAll(Pageable pageable, Long userId) {

		Page<Reservation> reservationPage = reservationRepository.findAll(pageable);

		if (userId > 0) {
			reservationPage = reservationRepository.findByClient(userId, pageable);
		}

		logger.info("Resource RESERVATION page found: " + "PAGE NUMBER [" + reservationPage.getNumber()
				+ "] - CONTENT: " + reservationPage.getContent());

		return reservationPage.map(x -> ReservationMapper.convertEntityToDto(x));

	}

	@Transactional(readOnly = true)
	public ReservationDTOv1 findById(Long id) {

		Optional<Reservation> user = reservationRepository.findById(id);

		var entity = user.orElseThrow(() -> new ResourceNotFoundException("Resource RESERVATION not found. ID " + id));

		logger.info("Resource RESERVATION found: " + entity.toString());

		return ReservationMapper.convertEntityToDto(entity);

	}

	@Transactional
	public ReservationDTOv1 reserveBooks(ReservationDTOv1 dto) {

		Reservation entity = new Reservation();

		entity.setStatus(ReservationStatus.IN_PROGRESS);

		entity.setClient(userRepository.getReferenceById(dto.getClient().getId()));

		entity.setWeeks(dto.getWeeks());

		entity.setMoment(Instant.now());

		entity.setDevolution(entity.devolutionCalc(dto.getWeeks()));

		entity.getBooks().clear();

		for (BookDTOv1 bookDTO : dto.getBooks()) {
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

		return ReservationMapper.convertEntityToDto(entity);

	}

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
