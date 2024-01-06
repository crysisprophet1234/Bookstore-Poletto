package com.poletto.bookstore.v3.mocks;

import java.time.LocalDate;

import com.poletto.bookstore.dto.v3.AddressDto;
import com.poletto.bookstore.dto.v3.PersonDto;
import com.poletto.bookstore.dto.v3.UserDto;
import com.poletto.bookstore.entities.Person;

public class PersonMocks {

	public static Person personMockEntity() {
		
		return new Person(
			1L,
			"testname",
			"name test",
			LocalDate.of(2000, 1, 1),
			"non-binary",
			"99999999999",
			"American",
			"https://picture.com",
			"99999999999",
			UserMocks.userMockEntity()
		);
		
	}
	
	public static Person personMockEntity(Long id) {
		
		return new Person(
			id,
			"testname" + id,
			"name test",
			LocalDate.of(2000, 1, 1),
			"non-binary",
			"99999999999",
			"American",
			"https://picture.com",
			"99999999999",
			UserMocks.userMockEntity()
		);
		
	}
	
	public static PersonDto insertPersonMock(UserDto userDto) {
		
		PersonDto personDto = new PersonDto();
		personDto.setFirstName("John");
		personDto.setLastName("Doe");
		personDto.setDateOfBirth(LocalDate.of(1990, 5, 15));
		personDto.setGender("non-binary");
		personDto.setPhone("99999999999");
		personDto.setCpf("12345678901");
		personDto.setUser(userDto);
		
		return personDto;
		
	}

	public static AddressDto insertAddressMock(PersonDto personDto) {
		
		AddressDto addressDto = new AddressDto();

    	addressDto.setCep("12345678");
    	addressDto.setAddressName("Oak Street");
    	addressDto.setNumber(42);
    	addressDto.setComplement("Unit 202");
    	addressDto.setDistrict("Green Valley");
    	addressDto.setCity("Metropolis");
    	addressDto.setState("SP");
    	
    	return addressDto;
		
	}
}
