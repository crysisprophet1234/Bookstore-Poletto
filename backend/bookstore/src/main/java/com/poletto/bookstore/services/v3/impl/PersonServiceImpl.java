package com.poletto.bookstore.services.v3.impl;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.controllers.v3.PersonController;
import com.poletto.bookstore.converter.v3.AddressMapperV3;
import com.poletto.bookstore.converter.v3.PersonMapperV3;
import com.poletto.bookstore.dto.v3.AddressDto;
import com.poletto.bookstore.dto.v3.PersonDto;
import com.poletto.bookstore.dto.v3.PersonUpdateDto;
import com.poletto.bookstore.entities.Address;
import com.poletto.bookstore.entities.Person;
import com.poletto.bookstore.entities.User;
import com.poletto.bookstore.exceptions.InvalidPersonForAddressUpdateException;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.repositories.v3.AddressRepository;
import com.poletto.bookstore.repositories.v3.PersonRepository;
import com.poletto.bookstore.repositories.v3.UserRepository;
import com.poletto.bookstore.services.v3.PersonService;

@Service("PersonServiceV3")
public class PersonServiceImpl implements PersonService {
	
	private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public PersonDto findById(Long personId) {
		
		Person entity = personRepository.findById(personId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource PERSON not found. ID " + personId));
		
		logger.info("Resource PERSON found: " + entity.toString());
		
		return personDtoWithLinks(PersonMapperV3.INSTANCE.personToPersonDto(entity));
		
	}
	
	@Override
	@Transactional
	public PersonDto create(Long userId, PersonDto personDto) {
		
		User userEntity = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource USER not found. ID " + userId));
		
		Person personEntity = PersonMapperV3.INSTANCE.personDtoToPerson(personDto);
		
		personEntity.setUser(userEntity);

		personEntity = personRepository.save(personEntity);
		
		return personDtoWithLinks(PersonMapperV3.INSTANCE.personToPersonDto(personEntity));
		
	}
	
	@Override
	@Transactional
	public PersonDto update(Long personId, PersonUpdateDto personUpdateDto) {
		
		Person personEntity = personRepository.findById(personId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource PERSON not found. ID " + personId));
		
		personEntity = PersonMapperV3.INSTANCE.updatePersonWithPersonUpdateDto(personEntity, personUpdateDto);
		
		personEntity = personRepository.save(personEntity);
		
		return personDtoWithLinks(PersonMapperV3.INSTANCE.personToPersonDto(personEntity));
		
	}
	
	@Override
	@Transactional
	public AddressDto addAddress(Long personId, AddressDto addressDto) {
		
		Person personEntity = personRepository.findById(personId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource PERSON not found. ID " + personId));
		
		Address address = AddressMapperV3.INSTANCE.addressDtoToAddress(addressDto);
		
		address.setPerson(personEntity);
		
		personEntity.getAddresses().add(address);
		
		personEntity = personRepository.save(personEntity);
		
		return AddressMapperV3.INSTANCE.addressToAddressDto(address);
		
	}

	@Override
	@Transactional
	public AddressDto updateAddress(Long personId, Long addressId, AddressDto addressDto) {
		
		Person personEntity = personRepository.findById(personId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource PERSON not found. ID " + personId));
		
		Address addressEntity = addressRepository.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource ADDRESS not found. ID " + addressId));
		
		if (addressEntity.getPerson().getId() != personEntity.getId()) {
			throw new InvalidPersonForAddressUpdateException(addressEntity.getId(), personEntity.getId());
		}
		
		addressEntity.setAddressName(addressDto.getAddressName());
		
		addressEntity.setComplement(addressDto.getComplement());
		
		return AddressMapperV3.INSTANCE.addressToAddressDto(addressEntity);
		
	}
	
	private PersonDto personDtoWithLinks(PersonDto personDto) {
		
		return personDto
				.add(linkTo(methodOn(PersonController.class)
						.findById(personDto.getKey())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(PersonController.class)
						.update(personDto.getKey(), null)).withRel("update").withType("PUT"))
				.add(linkTo(methodOn(PersonController.class)
						.addAddress(personDto.getKey(), null)).withRel("add address").withType("POST"))
				.add(linkTo(methodOn(PersonController.class)
						.updateAddress(personDto.getKey(), null, null)).withRel("update address").withType("PUT"));
						
	}
		
}
