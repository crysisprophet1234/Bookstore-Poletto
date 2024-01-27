package com.poletto.bookstore.controllers.v3.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poletto.bookstore.controllers.v3.AuthorController;
import com.poletto.bookstore.dto.v3.AuthorDto;
import com.poletto.bookstore.services.v3.AuthorService;

import jakarta.validation.constraints.Min;

@RestController("AuthorControllerV3")
@RequestMapping("/authors/v3")
public class AuthorControllerImpl implements AuthorController {
	
	@Autowired
	private AuthorService authorService;
	
	@GetMapping
	public ResponseEntity<List<AuthorDto>> findAll() {
		
		List<AuthorDto> list = authorService.findAll();
		
		return ResponseEntity.ok().body(list);
		
	}
	
	@GetMapping(path = "/paged")
	public ResponseEntity<Page<AuthorDto>> findAllPaged(Pageable pageable) {
		
		Page<AuthorDto> list = authorService.findAllPaged(pageable);
		
		return ResponseEntity.ok().body(list);
		
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<AuthorDto> findById(@PathVariable Long id) {
		
		AuthorDto dto = authorService.findById(id);
		
		return ResponseEntity.ok().body(dto);
		
	}

	@Override
	public ResponseEntity<AuthorDto> insert(@RequestBody AuthorDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<AuthorDto> update(@Min(1) Long id, @RequestBody AuthorDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
