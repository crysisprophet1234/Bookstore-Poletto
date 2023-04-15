package com.poletto.bookstore.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poletto.bookstore.dto.UserAuthDTO;
import com.poletto.bookstore.dto.UserDTO;
import com.poletto.bookstore.services.UserService;

@RestController
@RequestMapping (value = "/auth/v1")
public class AuthController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<UserDTO> insert(@RequestBody UserAuthDTO userDTO) {
		UserDTO dto = userService.insert(userDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@PostMapping("/authenticate")
	public ResponseEntity<UserAuthDTO> authenticate(@RequestBody UserAuthDTO userDTO) {
		UserAuthDTO dto = userService.authenticate(userDTO);
		return ResponseEntity.ok().body(dto);
	}

}
