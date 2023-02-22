package com.poletto.bookstore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poletto.bookstore.dto.BookDTO;
import com.poletto.bookstore.services.BookService;

@RestController
@RequestMapping(value = "/api/v1/books")
public class BookController {

	@Autowired
	private BookService bookService;

	@GetMapping
	public ResponseEntity<List<BookDTO>> findAll() {
		List<BookDTO> books = bookService.findAll();
		return ResponseEntity.ok().body(books);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<BookDTO> findById(@PathVariable Long id) {
		BookDTO book = bookService.findById(id);
		return ResponseEntity.ok().body(book);
	}

}
