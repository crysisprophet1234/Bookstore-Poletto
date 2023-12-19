package com.poletto.bookstore.controllers.v3;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poletto.bookstore.dto.v3.BookDto;
import com.poletto.bookstore.exceptions.exceptionresponse.ExceptionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

@RequestMapping(value = "/books/v3")
@Tag(name = "Book Controller V3", description = "Endpoints related to book resources management")
@Validated
public interface BookController {
	
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
	ResponseEntity<Page<BookDto>> findAllPaged(
		@Parameter(description = "Page index") @RequestParam(value = "page", defaultValue = "0") Integer page,
		@Parameter(description = "Page size") @RequestParam(value = "size", defaultValue = "12") Integer size,
		
		@Parameter(description = "Page sorting direction")
		@RequestParam(value = "sort", defaultValue = "asc")
		@Pattern(
			regexp = "^(?i)(ASC|DESC)$",
			message = "Parâmetro sort deve ser ASC ou DESC"
		)
		String sort,
		
		@Parameter(description = "Page sorting option")
		@RequestParam(value = "orderBy", defaultValue = "id")
		@Pattern(
			regexp = "^(?i)(ID|TITLE|AUTHOR|STATUS)$",
			message = "Parâmetro orderBy deve ser ID, Title, Author (name) ou STATUS"
		)
		String orderBy,
		
		@Parameter(description = "Book category ID") @RequestParam(value = "categoryId", defaultValue = "") @Min(1) Long categoryId,
		@Parameter(description = "Book or author name") @RequestParam(value = "name", defaultValue = "") String name,
		
		@Parameter(description = "Book status")
		@RequestParam(value = "status", defaultValue = "all")	
		@Pattern(
			regexp = "^(?i)(ACTIVE|INACTIVE|ALL)$",
			message = "Parâmetro status deve ser ACTIVE, INACTIVE ou ALL"
		)
		String status
	);
	
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
	ResponseEntity<BookDto> findById(
		@Parameter(description = "Book ID")
		@PathVariable @Min(1) Long id
	);
	
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
	ResponseEntity<BookDto> insert(
		@Parameter(
			description = "Payload with new book data",
			content = @Content(schema = @Schema(implementation = BookDto.class))
		)
		@RequestBody @Valid BookDto dto
	);
	
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
	ResponseEntity<BookDto> update(
		@Parameter(description = "Book ID")
		@PathVariable @Min(1) Long id,
		@Parameter(
			description = "Payload with new book data to be updated",
			content = @Content(schema = @Schema(implementation = BookDto.class))
		)
		@RequestBody @Valid BookDto dto
	);
	
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
	ResponseEntity<Void> delete(
		@Parameter(description = "Book ID")
		@PathVariable @Min(1) Long id
	);

}
