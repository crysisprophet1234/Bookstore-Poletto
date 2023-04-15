package com.poletto.bookstore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poletto.bookstore.dto.AuthorDTO;
import com.poletto.bookstore.services.AuthorService;

@Controller
@RequestMapping("/authors/v1")
public class AuthorController {
	
	@Autowired
	private AuthorService authorService;
	
	@Deprecated
	@GetMapping
	public ResponseEntity<List<AuthorDTO>> findAll() {
		List<AuthorDTO> list = authorService.findAll();		
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/paged")
	public ResponseEntity<Page<AuthorDTO>> findAllPaged(Pageable pageable) {
		Page<AuthorDTO> list = authorService.findAllPaged(pageable);		
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<AuthorDTO> findById(@PathVariable Long id) {
		AuthorDTO dto = authorService.findById(id);
		return ResponseEntity.ok().body(dto);
	}

}
