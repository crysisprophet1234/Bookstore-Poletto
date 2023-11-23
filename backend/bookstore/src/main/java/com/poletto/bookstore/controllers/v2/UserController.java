package com.poletto.bookstore.controllers.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poletto.bookstore.dto.v2.UserAuthDTOv2;
import com.poletto.bookstore.dto.v2.UserDTOv2;
import com.poletto.bookstore.services.v2.UserService;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;

@RestController("UserControllerV2")
@RequestMapping(value = "/users/v2")
@Hidden
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<Page<UserDTOv2>> findAllPaged(Pageable pageable) {
		Page<UserDTOv2> list = userService.findAllPaged(pageable);
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<UserDTOv2> findById(@PathVariable Long id) {
		UserDTOv2 userDTO = userService.findById(id);
		return ResponseEntity.ok().body(userDTO);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<UserDTOv2> update(@PathVariable Long id, @RequestBody @Valid UserAuthDTOv2 dto) {
		UserDTOv2 userDTO = userService.update(id, dto);
		return ResponseEntity.ok().body(userDTO);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		userService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
