package com.poletto.bookstore.services.v3.mocks;

import java.time.Instant;
import java.time.LocalDate;

import com.poletto.bookstore.dto.v2.ReservationDTOv2;
import com.poletto.bookstore.entities.Reservation;
import com.poletto.bookstore.entities.enums.ReservationStatus;

public class ReservationMocks {
	
	public static ReservationDTOv2 reservationMockDto(Long id) {
		
		return new ReservationDTOv2(
				id,
				Instant.now(),
				ReservationStatus.IN_PROGRESS,
				LocalDate.now().plusMonths(1),
				4,
				UserMocks.userMockDto());
		
	}
	
	public static ReservationDTOv2 reservationMockDto() {
		
		return new ReservationDTOv2(
				1L,
				Instant.now(),
				ReservationStatus.IN_PROGRESS,
				LocalDate.now().plusMonths(1),
				4,
				UserMocks.userMockDto());
		
	}
	
	public static Reservation reservationMockEntity () {
		
		return new Reservation(
				1L,
				ReservationStatus.IN_PROGRESS,
				Instant.now(),
				UserMocks.userMockEntity(),
				4
		);
		
	}
	
	public static Reservation reservationMockEntity (Long id) {
		
		return new Reservation(
				id,
				ReservationStatus.IN_PROGRESS,
				Instant.now(),
				UserMocks.userMockEntity(),
				4
		);
		
	}

	public static ReservationDTOv2 insertReservationMockDto() {

		ReservationDTOv2 dto = new ReservationDTOv2();

		dto.setClient(UserMocks.userMockDto());
		dto.setWeeks(2);
		dto.getBooks().add(BookMocks.bookMockDto());

		return dto;

	}

}
