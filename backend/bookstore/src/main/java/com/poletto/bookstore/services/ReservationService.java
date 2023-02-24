package com.poletto.bookstore.services;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.dto.BookDTO;
import com.poletto.bookstore.dto.ReservationDTO;
import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.BookReservation;
import com.poletto.bookstore.entities.Reservation;
import com.poletto.bookstore.entities.enums.BookStatus;
import com.poletto.bookstore.entities.enums.ReservationStatus;
import com.poletto.bookstore.repositories.BookRepository;
import com.poletto.bookstore.repositories.BookReservationRepository;
import com.poletto.bookstore.repositories.ReservationRepository;
import com.poletto.bookstore.repositories.UserRepository;
import com.poletto.bookstore.services.exceptions.InvalidStatus;
import com.poletto.bookstore.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private BookReservationRepository bookReservationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	@Transactional(readOnly = true)
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
	public ReservationDTO reserveBooks(ReservationDTO dto) {

		Reservation entity = new Reservation();

		copyDtoToEntity(dto, entity);

		changeStatusBook(entity, BookStatus.BOOKED);

		entity.setStatus(ReservationStatus.IN_PROGRESS);

		entity = reservationRepository.save(entity);

		for (BookReservation book : entity.getBooks()) {
			bookReservationRepository.save(book);
		}

		return new ReservationDTO(entity);

	}

	@Transactional
	public void returnBooks(Long id) {

		try {

			Reservation entity = reservationRepository.getReferenceById(id);
			
			if (entity.getStatus().equals(ReservationStatus.FINISHED)) {
				throw new InvalidStatus(entity);
			}

			changeStatusBook(entity, BookStatus.AVAILABLE);

			entity.setStatus(ReservationStatus.FINISHED);

			reservationRepository.save(entity);

		} catch (EntityNotFoundException e) {

			throw new ResourceNotFoundException(id);

		}

	}

	private void changeStatusBook(Reservation entity, BookStatus status) {

		Set<Book> booksOld = entity.getBooks().stream().map(x -> x.getBook()).collect(Collectors.toSet());

		Set<Book> booksNew = new HashSet<>();

		for (Book book : booksOld) {
			if (book.getStatus().equals(status)) {
				throw new InvalidStatus(book);
			}
			book.setStatus(status);
			booksNew.add(book);
		}

		booksNew.forEach(x -> bookRepository.save(x));

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
			entity.getBooks().add(new BookReservation(entity, book));

		}
	}

}
