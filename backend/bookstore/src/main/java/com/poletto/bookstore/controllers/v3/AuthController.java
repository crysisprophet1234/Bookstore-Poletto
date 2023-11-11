package com.poletto.bookstore.controllers.v3;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poletto.bookstore.dto.v2.UserAuthDTOv2;
import com.poletto.bookstore.dto.v2.UserDTOv2;
import com.poletto.bookstore.services.v2.EmailService;
import com.poletto.bookstore.services.v3.AuthService;

import jakarta.validation.Valid;

@RestController("AuthControllerV3")
@RequestMapping(value = "/auth/v3")
public class AuthController {

	@Autowired
	private AuthService authService;
	
	@Autowired
	private EmailService emailService;

	@PostMapping("/register")
	public ResponseEntity<UserDTOv2> insert(@RequestBody @Valid UserAuthDTOv2 userDTO) {
		UserDTOv2 dto = authService.register(userDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		emailService.sendEmailFromTemplate(dto.getEmail(), "Confirmação de criação de conta", dto.getFirstname() + " " + dto.getLastname());
		return ResponseEntity.created(uri).body(dto);
	}

	@PostMapping("/authenticate")
	public ResponseEntity<UserAuthDTOv2> authenticate(@RequestBody UserAuthDTOv2 userDTO) {
		UserAuthDTOv2 dto = authService.authenticate(userDTO);
		return ResponseEntity.ok().body(dto);
	}

}
