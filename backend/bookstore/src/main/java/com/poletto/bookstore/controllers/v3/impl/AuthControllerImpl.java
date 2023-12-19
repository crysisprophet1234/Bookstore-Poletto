package com.poletto.bookstore.controllers.v3.impl;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poletto.bookstore.controllers.v3.AuthController;
import com.poletto.bookstore.dto.v3.UserDto;
import com.poletto.bookstore.services.v3.AuthService;

@RestController("AuthControllerV3")
@RequestMapping(value = "/v3/auth")
public class AuthControllerImpl implements AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<UserDto> insert(@RequestBody UserDto userDTO) {
		
		UserDto dto = authService.register(userDTO);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getKey()).toUri();
		
		return ResponseEntity.created(uri).body(dto);
		
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<UserDto> authenticate(@RequestBody UserDto userDTO) {
		
		UserDto dto = authService.authenticate(userDTO);
		
		return ResponseEntity.ok().body(dto);
		
	}
	
}
