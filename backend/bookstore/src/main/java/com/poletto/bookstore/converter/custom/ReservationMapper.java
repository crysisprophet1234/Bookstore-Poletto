package com.poletto.bookstore.converter.custom;

import org.springframework.stereotype.Service;

import com.poletto.bookstore.converter.DozerMapperConverter;
import com.poletto.bookstore.dto.v1.ReservationDTOv1;
import com.poletto.bookstore.dto.v1.UserDTOv1;
import com.poletto.bookstore.dto.v1.BookDTOv1;
import com.poletto.bookstore.entities.BookReservation;
import com.poletto.bookstore.entities.Reservation;
import com.poletto.bookstore.entities.User;
import com.poletto.bookstore.entities.enums.ReservationStatus;

@Service
public class ReservationMapper {

	public static ReservationDTOv1 convertEntityToDto(Reservation reservation) {
		
		ReservationDTOv1 dto = new ReservationDTOv1();
		dto.setId(reservation.getId());
		dto.setDevolution(reservation.getDevolution());
		dto.setMoment(reservation.getMoment());
		dto.setWeeks(reservation.getWeeks());
		dto.setStatus(reservation.getStatus().toString());
		dto.setClient(DozerMapperConverter.parseObject(reservation.getClient(), UserDTOv1.class));
		
		dto.getBooks().clear();
		
		for (BookReservation bookReservation : reservation.getBooks()) {
			dto.getBooks().add(BookMapper.convertEntityToDto(bookReservation.getBook()));
		}
		
		return dto;
		
	}

	public static Reservation convertDtoToEntity(ReservationDTOv1 dto) {
		
		Reservation reservation = new Reservation();
		reservation.setId(dto.getId());
		reservation.setDevolution(dto.getDevolution());
		reservation.setMoment(dto.getMoment());
		reservation.setWeeks(dto.getWeeks());
		reservation.setStatus(ReservationStatus.valueOf(dto.getStatus()));
		reservation.setClient(DozerMapperConverter.parseObject(dto.getClient(), User.class));
		
		reservation.getBooks().clear();
		
		for (BookDTOv1 bookDTO : dto.getBooks()) {
			reservation.getBooks().add(new BookReservation(reservation, BookMapper.convertDtoToEntity(bookDTO)));
		}
		
		return reservation;

	}

}
