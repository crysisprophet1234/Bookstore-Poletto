package com.poletto.bookstore.auth;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poletto.bookstore.dto.UserDTO;
import com.poletto.bookstore.dto.UserInsertDTO;
import com.poletto.bookstore.dto.UserLoginDTO;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping ("/register")
	public ResponseEntity<UserDTO> register(@RequestBody UserInsertDTO userDTO) {
		UserDTO dto = authenticationService.register(userDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PostMapping ("/authenticate")
	public ResponseEntity<UserLoginDTO> register(@RequestBody UserLoginDTO userDTO) {
		UserLoginDTO dto = authenticationService.authenticate(userDTO);
		return ResponseEntity.ok().body(dto);
	}

}
