package com.poletto.bookstore.services.v3;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.dto.v3.AddressDto;
import com.poletto.bookstore.dto.v3.PersonDto;
import com.poletto.bookstore.dto.v3.PersonUpdateDto;

@Service("PersonServiceV3")
public interface PersonService {
	
	@Transactional(readOnly = true)
	PersonDto findById(Long personId);
	
	@Transactional
	PersonDto create(Long userId, PersonDto personDto);
	
	@Transactional
	PersonDto update(Long personId, PersonUpdateDto personUpdateDto);
	
	@Transactional
	AddressDto addAddress(Long personId, AddressDto addressDto);
	
	@Transactional
	AddressDto updateAddress(Long personId, Long addressId, AddressDto addressDto);

}
