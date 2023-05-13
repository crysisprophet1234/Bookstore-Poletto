package com.poletto.bookstore.controllers.v2;

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
import com.poletto.bookstore.services.v1.UserServiceV1;

@RestController
@RequestMapping (value = "/auth/v2")
public class AuthControllerV2 {

	@Autowired
	private UserServiceV1 userService;

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
