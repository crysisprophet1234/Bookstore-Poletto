package com.poletto.bookstore.converter.v3;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import com.poletto.bookstore.dto.v3.AddressDto;
import com.poletto.bookstore.entities.Address;

@Component
@Mapper(componentModel = "spring")
public interface AddressMapperV3 {
	
	AddressMapperV3 INSTANCE = Mappers.getMapper(AddressMapperV3.class);
	
	@Mapping(target = "id", source = "id")
	AddressDto addressToAddressDto(Address address);
	
	@Mapping(target = "id", source = "id")
	Address addressDtoToAddress(AddressDto addressDto);

}
