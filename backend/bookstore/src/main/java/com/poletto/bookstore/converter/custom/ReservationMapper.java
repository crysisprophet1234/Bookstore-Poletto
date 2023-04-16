package com.poletto.bookstore.converter.custom;

import org.springframework.stereotype.Service;

import com.poletto.bookstore.converter.DozerMapperConverter;
import com.poletto.bookstore.dto.BookDTO;
import com.poletto.bookstore.dto.ReservationDTO;
import com.poletto.bookstore.dto.UserDTO;
import com.poletto.bookstore.entities.BookReservation;
import com.poletto.bookstore.entities.Reservation;
import com.poletto.bookstore.entities.User;
import com.poletto.bookstore.entities.enums.ReservationStatus;

@Service
public class ReservationMapper {

	public static ReservationDTO convertEntityToDto(Reservation reservation) {
		
		ReservationDTO dto = new ReservationDTO();
		dto.setId(reservation.getId());
		dto.setDevolution(reservation.getDevolution());
		dto.setMoment(reservation.getMoment());
		dto.setWeeks(reservation.getWeeks());
		dto.setStatus(reservation.getStatus().toString());
		dto.setClient(DozerMapperConverter.parseObject(reservation.getClient(), UserDTO.class));
		
		dto.getBooks().clear();
		
		for (BookReservation bookReservation : reservation.getBooks()) {
			dto.getBooks().add(BookMapper.convertEntityToDto(bookReservation.getBook()));
		}
		
		return dto;
		
	}

	public static Reservation convertDtoToEntity(ReservationDTO dto) {
		
		Reservation reservation = new Reservation();
		reservation.setId(dto.getId());
		reservation.setDevolution(dto.getDevolution());
		reservation.setMoment(dto.getMoment());
		reservation.setWeeks(dto.getWeeks());
		reservation.setStatus(ReservationStatus.valueOf(dto.getStatus()));
		reservation.setClient(DozerMapperConverter.parseObject(dto.getClient(), User.class));
		
		reservation.getBooks().clear();
		
		for (BookDTO bookDTO : dto.getBooks()) {
			reservation.getBooks().add(new BookReservation(reservation, BookMapper.convertDtoToEntity(bookDTO)));
		}
		
		return reservation;

	}

}
