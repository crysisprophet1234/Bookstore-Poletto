package com.poletto.bookstore.converter.v2;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import com.poletto.bookstore.dto.v2.ReservationDTOv2;
import com.poletto.bookstore.entities.Reservation;

@Component
@Mapper(componentModel = "spring")
public interface ReservationMapper {
	
	ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

	@Mapping(target = "id", source = "id")
	ReservationDTOv2 reservationToReservationDto(Reservation reservation);
	
	@Mapping(target = "id", source = "id")
	Reservation reservationDtoToReservation(ReservationDTOv2 reservationDto);


}