package com.poletto.bookstore.converter.v3;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import com.poletto.bookstore.dto.v3.ReservationDto;
import com.poletto.bookstore.entities.Reservation;

@Component
@Mapper(componentModel = "spring")
public interface ReservationMapperV3 {
	
	ReservationMapperV3 INSTANCE = Mappers.getMapper(ReservationMapperV3.class);

	@Mapping(target = "key", source = "id")
	@Mapping(target = "client.user", ignore = true)
	ReservationDto reservationToReservationDto(Reservation reservation);
	
	@Mapping(target = "id", source = "key")
	Reservation reservationDtoToReservation(ReservationDto reservationDto);


}