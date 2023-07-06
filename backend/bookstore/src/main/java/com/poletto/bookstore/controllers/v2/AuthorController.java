package com.poletto.bookstore.controllers.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poletto.bookstore.dto.v2.AuthorDTOv2;
import com.poletto.bookstore.services.v2.AuthorService;

@RestController("AuthorControllerV2")
@RequestMapping("/authors/v2")
public class AuthorController {
	
	@Autowired
	private AuthorService authorService;
	
	@GetMapping
	public ResponseEntity<Page<AuthorDTOv2>> findAllPaged(Pageable pageable) {
		Page<AuthorDTOv2> list = authorService.findAllPaged(pageable);		
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<AuthorDTOv2> findById(@PathVariable Long id) {
		AuthorDTOv2 dto = authorService.findById(id);
		return ResponseEntity.ok().body(dto);
	}

}
