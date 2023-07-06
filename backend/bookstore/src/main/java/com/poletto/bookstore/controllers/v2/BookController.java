package com.poletto.bookstore.controllers.v2;

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

import com.poletto.bookstore.dto.v2.BookDTOv2;
import com.poletto.bookstore.services.v2.BookService;
import com.poletto.bookstore.util.MediaType;

import jakarta.validation.Valid;

@RestController("BookControllerV2")
@RequestMapping(value = "/books/v2")
public class BookController {

	@Autowired
	private BookService bookService;
	
	@GetMapping(
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML },
			consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	public ResponseEntity<Page<BookDTOv2>> findAllPaged(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "12") Integer size,
			@RequestParam(value = "sort", defaultValue = "asc") String sort,
			@RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
			@RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
			@RequestParam(value = "authorId", defaultValue = "0") Long authorId,
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "booked", defaultValue = "") String booked) {

		Direction sortDirection = "desc".equalsIgnoreCase(sort) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, orderBy));

		return ResponseEntity.ok(bookService.findAllPaged(pageable, categoryId, name.trim(), booked));

	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<BookDTOv2> findById(@PathVariable Long id) {
		BookDTOv2 entity = bookService.findById(id);
		return ResponseEntity.ok().body(entity);
	}
	
	@PostMapping
	public ResponseEntity<BookDTOv2> insert(@RequestBody @Valid BookDTOv2 dto) {
		dto = bookService.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<BookDTOv2> update(@PathVariable Long id, @RequestBody @Valid BookDTOv2 dto) {
		BookDTOv2 bookDTO = bookService.update(id, dto);
		return ResponseEntity.ok().body(bookDTO);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		bookService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
