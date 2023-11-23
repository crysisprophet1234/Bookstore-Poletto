package com.poletto.bookstore.controllers.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poletto.bookstore.dto.v1.AuthorDTOv1;
import com.poletto.bookstore.services.v1.AuthorService;

import io.swagger.v3.oas.annotations.Hidden;

@Controller
@RequestMapping("/authors/v1")
@Hidden
public class AuthorController {
	
	@Autowired
	private AuthorService authorService;
	
	@Deprecated
	@GetMapping
	public ResponseEntity<List<AuthorDTOv1>> findAll() {
		List<AuthorDTOv1> list = authorService.findAll();		
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/paged")
	public ResponseEntity<Page<AuthorDTOv1>> findAllPaged(Pageable pageable) {
		Page<AuthorDTOv1> list = authorService.findAllPaged(pageable);		
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<AuthorDTOv1> findById(@PathVariable Long id) {
		AuthorDTOv1 dto = authorService.findById(id);
		return ResponseEntity.ok().body(dto);
	}

}
