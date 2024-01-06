package com.poletto.bookstore.controllers.v3.impl;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poletto.bookstore.controllers.v3.PersonController;
import com.poletto.bookstore.dto.v3.AddressDto;
import com.poletto.bookstore.dto.v3.PersonDto;
import com.poletto.bookstore.dto.v3.PersonUpdateDto;
import com.poletto.bookstore.services.v3.PersonService;

@RestController("PersonControllerV3")
@RequestMapping(value = "/v3/persons")
public class PersonControllerImpl implements PersonController {
	
	@Autowired
	private PersonService personService;

	@Override
	@GetMapping(value = "/{personId}")
	public ResponseEntity<PersonDto> findById(@PathVariable Long personId) {
		
		PersonDto personDto = personService.findById(personId);
		
		return ResponseEntity.ok().body(personDto);
	}

	@Override
	@PostMapping(value = "/create")
	public ResponseEntity<PersonDto> create(@RequestBody PersonDto personDto) {
		
		personDto = personService.create(personDto.getUser().getKey(), personDto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{personId}").buildAndExpand(personDto.getKey()).toUri();
		
		return ResponseEntity.created(uri).body(personDto);
	}

	@Override
	@PutMapping(value = "/{personId}/update")
	public ResponseEntity<PersonDto> update(@PathVariable Long personId, @RequestBody PersonUpdateDto personUpdateDto) {
		
		PersonDto updatedPersonDto = personService.update(personId, personUpdateDto);
		
		return ResponseEntity.ok().body(updatedPersonDto);
	}

	@Override
	@PostMapping(value = "/{personId}/addresses/create")
	public ResponseEntity<AddressDto> addAddress(@PathVariable Long personId, @RequestBody AddressDto addressDto) {
		
		addressDto = personService.addAddress(personId, addressDto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{personId}").buildAndExpand(personId).toUri();
		
		return ResponseEntity.created(uri).body(addressDto);
	}

	@Override
	@PutMapping(value = "/{personId}/addresses/{addressId}/update")
	public ResponseEntity<AddressDto> updateAddress(Long personId, Long addressId, @RequestBody AddressDto addressDto) {
		
		AddressDto updatedAddressDto = personService.updateAddress(personId, addressId, addressDto);
		
		return ResponseEntity.ok().body(updatedAddressDto);
	}

}
