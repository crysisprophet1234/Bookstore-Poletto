package com.poletto.bookstore.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poletto.bookstore.dto.BookDTO;
import com.poletto.bookstore.services.BookService;

@RestController
@RequestMapping(value = "/api/v1/books")
public class BookController {

	@Autowired
	private BookService bookService;
	
	@GetMapping
	public ResponseEntity<List<BookDTO>> findAll(@RequestParam Optional<Integer> booked) {
		List<BookDTO> books = bookService.findAll(booked.orElse(-1));
		return ResponseEntity.ok().body(books);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<BookDTO> findById(@PathVariable Long id) {
		BookDTO entity = bookService.findById(id);
		return ResponseEntity.ok().body(entity);
	}

	@PostMapping
	public ResponseEntity<BookDTO> insert(@RequestBody BookDTO dto) {
		dto = bookService.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<BookDTO> update(@PathVariable Long id, @RequestBody BookDTO dto) {
		BookDTO bookDTO = bookService.update(id, dto);
		return ResponseEntity.ok().body(bookDTO);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		bookService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
