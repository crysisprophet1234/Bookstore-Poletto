package com.poletto.bookstore.controllers.v2;

import java.util.List;

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

import com.poletto.bookstore.dto.v1.UserAuthDTOv1;
import com.poletto.bookstore.dto.v1.UserDTOv1;
import com.poletto.bookstore.services.v2.UserServiceV2;

@RestController
@RequestMapping(value = "/users/v2")
public class UserControllerV2 {

	@Autowired
	private UserServiceV2 userService;

	@GetMapping
	@Deprecated
	public ResponseEntity<List<UserDTOv1>> findAll() {
		List<UserDTOv1> list = userService.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/paged")
	public ResponseEntity<Page<UserDTOv1>> findAllPaged(Pageable pageable) {
		Page<UserDTOv1> list = userService.findAllPaged(pageable);
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<UserDTOv1> findById(@PathVariable Long id) {
		UserDTOv1 userDTO = userService.findById(id);
		return ResponseEntity.ok().body(userDTO);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<UserDTOv1> update(@PathVariable Long id, @RequestBody UserAuthDTOv1 dto) {
		UserDTOv1 userDTO = userService.update(id, dto);
		return ResponseEntity.ok().body(userDTO);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		userService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
