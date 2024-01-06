package com.poletto.bookstore.converter.v3;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import com.poletto.bookstore.dto.v3.PersonDto;
import com.poletto.bookstore.dto.v3.PersonUpdateDto;
import com.poletto.bookstore.entities.Person;

@Component
@Mapper(componentModel = "spring")
public interface PersonMapperV3 {
	
	PersonMapperV3 INSTANCE = Mappers.getMapper(PersonMapperV3.class);
	
	@Mapping(target = "key", source = "id")
	@Mapping(target = "user.key", source = "user.id")
	PersonDto personToPersonDto(Person person);
	
	@Mapping(target = "id", source = "key")
	@Mapping(target = "user.id", source = "user.key")
	Person personDtoToPerson(PersonDto personDto);

	@Mapping(target = "id", source = "person.id")
	@Mapping(target = "user", source = "person.user")
	@Mapping(target = "cpf", source = "person.cpf")
	@Mapping(target = "dateOfBirth", source = "person.dateOfBirth")
	@Mapping(target = "profilePictureUrl", source = "personUpdateDto.profilePictureUrl")
	@Mapping(target = "phone", source = "personUpdateDto.phone")
	@Mapping(target = "nationality", source = "personUpdateDto.nationality")
	@Mapping(target = "firstName", source = "personUpdateDto.firstName")
	@Mapping(target = "lastName", source = "personUpdateDto.lastName")
	@Mapping(target = "gender", source = "personUpdateDto.gender")
	Person updatePersonWithPersonUpdateDto(Person person, PersonUpdateDto personUpdateDto);

}
