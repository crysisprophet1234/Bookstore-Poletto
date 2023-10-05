package com.poletto.bookstore.services.v3.mocks;

import com.poletto.bookstore.dto.v2.ReservationDTOv2;

public class ReservationMocks {

	public static ReservationDTOv2 insertReservationDto() {

		ReservationDTOv2 dto = new ReservationDTOv2();

		dto.setClient(UserMocks.userMock());
		dto.setWeeks(2);
		dto.getBooks().add(BookMocks.bookMock());

		return dto;

	}

}
