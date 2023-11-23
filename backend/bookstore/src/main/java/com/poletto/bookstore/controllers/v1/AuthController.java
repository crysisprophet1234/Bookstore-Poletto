package com.poletto.bookstore.controllers.v1;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poletto.bookstore.dto.v1.UserAuthDTOv1;
import com.poletto.bookstore.dto.v1.UserDTOv1;
import com.poletto.bookstore.services.v1.UserService;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
@RequestMapping (value = "/auth/v1")
@Hidden
public class AuthController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<UserDTOv1> insert(@RequestBody UserAuthDTOv1 userDTO) {
		UserDTOv1 dto = userService.insert(userDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@PostMapping("/authenticate")
	public ResponseEntity<UserAuthDTOv1> authenticate(@RequestBody UserAuthDTOv1 userDTO) {
		UserAuthDTOv1 dto = userService.authenticate(userDTO);
		return ResponseEntity.ok().body(dto);
	}

}
