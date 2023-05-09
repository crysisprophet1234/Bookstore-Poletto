package com.poletto.bookstore.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

import com.poletto.bookstore.dto.v1.BookDTO;
import com.poletto.bookstore.services.BookService;
import com.poletto.bookstore.util.MediaType;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/books/v1")
public class BookController {

	@Autowired
	private BookService bookService;

	@GetMapping(
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML },
			consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	public ResponseEntity<Page<BookDTO>> findAllPaged(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "12") Integer size,
			@RequestParam(value = "sort", defaultValue = "asc") String sort,
			@RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
			@RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "booked", defaultValue = "") String booked) {

		Direction sortDirection = "desc".equalsIgnoreCase(sort) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, orderBy));

		return ResponseEntity.ok(bookService.findAllPaged(pageable, categoryId, name.trim(), booked));

	}
	
	@GetMapping(
			value = "/v2",
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML },
			consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	public ResponseEntity<Page<com.poletto.bookstore.dto.v2.BookDTO>> findAllPagedV2(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "12") Integer size,
			@RequestParam(value = "sort", defaultValue = "asc") String sort,
			@RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
			@RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "booked", defaultValue = "") String booked) {

		Direction sortDirection = "desc".equalsIgnoreCase(sort) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, orderBy));

		return ResponseEntity.ok(bookService.findAllPagedV2(pageable, categoryId, name.trim(), booked));

	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<BookDTO> findById(@PathVariable Long id) {
		BookDTO entity = bookService.findById(id);
		return ResponseEntity.ok().body(entity);
	}
	
	@PostMapping
	public ResponseEntity<BookDTO> insert(@RequestBody @Valid BookDTO dto) {
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
