package com.poletto.bookstore.controllers.v3.impl;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poletto.bookstore.controllers.v3.BookController;
import com.poletto.bookstore.dto.v3.BookDto;
import com.poletto.bookstore.dto.v3.BookStatusUpdateDto;
import com.poletto.bookstore.dto.v3.BookUpdateDto;
import com.poletto.bookstore.services.v3.BookService;

@RestController("BookControllerV3")
@RequestMapping(value = "/v3/books")
public class BookControllerImpl implements BookController {

	@Autowired
	private BookService bookService;
	
	@Override
	@GetMapping
	public ResponseEntity<Page<BookDto>> findAllPaged(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "12") Integer size,
			@RequestParam(value = "sort", defaultValue = "asc") String sort,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "categoryId", defaultValue = "") Long categoryId,
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "status", defaultValue = "all") String status
		) {
		
		orderBy = orderBy.equalsIgnoreCase("author") ? "author.name" : orderBy;
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.valueOf(sort.toUpperCase()), orderBy));
		
		Page<BookDto> bookDtoPage = bookService.findAll(pageable, categoryId, name.trim(), status);

		return ResponseEntity.ok(bookDtoPage);

	}

	@Override
	@GetMapping(value = "/{bookId}")
	public ResponseEntity<BookDto> findById(@PathVariable Long bookId) {
		
		BookDto bookEntity = bookService.findById(bookId);
		
		return ResponseEntity.ok().body(bookEntity);
		
	}
	
	@Override
	@PostMapping(value = "/create")
	public ResponseEntity<BookDto> insert(@RequestBody BookDto bookDto) {
		
		BookDto insertedBookDto = bookService.insert(bookDto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(insertedBookDto.getId()).toUri();
		
		return ResponseEntity.created(uri).body(insertedBookDto);
		
	}

	@Override
	@PutMapping(value = "/{bookId}/update")
	public ResponseEntity<BookDto> update(@PathVariable Long bookId, @RequestBody BookUpdateDto bookDto) {
		
		BookDto updatedBookDto = bookService.update(bookId, bookDto);
		
		return ResponseEntity.ok().body(updatedBookDto);
		
	}
	
	@Override
	@PutMapping(value = "/{bookId}/change-status")
	public ResponseEntity<BookDto> updateBookStatus(@PathVariable Long bookId, @RequestBody BookStatusUpdateDto bookStatusUpdateDto) {
		
		BookDto bookDto = bookService.updateBookStatus(bookId, bookStatusUpdateDto);
		
		return ResponseEntity.ok().body(bookDto);
		
	}

}
