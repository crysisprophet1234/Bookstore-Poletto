package com.poletto.bookstore.controllers.v3;

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
import com.poletto.bookstore.exceptions.exceptionresponse.ExceptionResponse;
import com.poletto.bookstore.services.v3.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController("BookControllerV3")
@RequestMapping(value = "/books/v3")
@Tag(name = "Book Controller V3", description = "Endpoints related to book resources management")
public class BookController {

	@Autowired
	private BookService bookService;
	
	@GetMapping
	@Operation(
		summary = "Returns a page of books",
		description = "Get book page filtered by the parameters provided",
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "200",
				useReturnTypeSchema = true
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Internal error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
		}
	)
	public ResponseEntity<Page<BookDTOv2>> findAllPaged(
			@Parameter(description = "Page index") @RequestParam(value = "page", defaultValue = "0") Integer page,
			@Parameter(description = "Page size") @RequestParam(value = "size", defaultValue = "12") Integer size,
			@Parameter(description = "Page sorting direction") @RequestParam(value = "sort", defaultValue = "asc") String sort,
			@Parameter(description = "Page sorting option") @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@Parameter(description = "Book category ID") @RequestParam(value = "categoryId", defaultValue = "") Long categoryId,
			@Parameter(description = "Book or author name") @RequestParam(value = "name", defaultValue = "") String name,
			@Parameter(description = "Book status") @RequestParam(value = "status", defaultValue = "all") String status
		) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.valueOf(sort.toUpperCase()), orderBy));
		
		Page<BookDTOv2> books = bookService.findAllPaged(pageable, categoryId, name.trim(), status);

		return ResponseEntity.ok(books);

	}

	@GetMapping(value = "/{id}")
	@Operation(
		summary = "Returns a book by ID",
		description = "Get book details based on the ID provided",
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "200",
				useReturnTypeSchema = true
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Internal error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
		}
	)
	public ResponseEntity<BookDTOv2> findById(
			@Parameter(description = "Book ID")
			@PathVariable Long id
		) {
		BookDTOv2 entity = bookService.findById(id);
		return ResponseEntity.ok().body(entity);
	}
	
	@PostMapping
	@Operation(
		summary = "Creates a new book",
		description = "Creates a new book with provided data and then return new book details",
		security = @SecurityRequirement(name = "bearerAuth"),
		responses = {
			@ApiResponse(
				description = "Created",
				responseCode = "201",
				useReturnTypeSchema = true
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Unprocessable Entity", responseCode = "422", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Internal error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
		}
	)
	public ResponseEntity<BookDTOv2> insert(
			@Parameter(
				description = "Payload with new book data",
				content = @Content(schema = @Schema(implementation = BookDTOv2.class))
			)
			@RequestBody @Valid BookDTOv2 dto
		) {
		dto = bookService.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@PutMapping(value = "/{id}")
	@Operation(
		summary = "Updates book by ID",
		description = "Updates a book by ID and then return updated data",
		security = @SecurityRequirement(name = "bearerAuth"),
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "200",
				useReturnTypeSchema = true
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Unprocessable Entity", responseCode = "422", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Internal error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
		}
	)
	public ResponseEntity<BookDTOv2> update(
			@Parameter(description = "Book ID")
			@PathVariable Long id,
			@Parameter(
				description = "Payload with new book data to be updated",
				content = @Content(schema = @Schema(implementation = BookDTOv2.class))
			)
			@RequestBody @Valid BookDTOv2 dto
		) {
		BookDTOv2 bookDTO = bookService.update(id, dto);
		return ResponseEntity.ok().body(bookDTO);
	}

	@DeleteMapping(value = "/{id}")
	@Operation(
		summary = "Deletes a book by ID",
		description = "Deletes book based on the ID provided",
		security = @SecurityRequirement(name = "bearerAuth"),
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "204"
			),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Internal error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
		}
	)
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		bookService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
