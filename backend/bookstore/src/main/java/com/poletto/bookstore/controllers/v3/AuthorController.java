package com.poletto.bookstore.controllers.v3;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poletto.bookstore.dto.v3.AuthorDto;
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

@RequestMapping("/authors/v3")
@Tag(name = "Author Controller V3", description = "Endpoints related to author resources management")
@Validated
public interface AuthorController {
	
	//TODO: add constraint validations	
	
	@GetMapping
	@Operation(
		summary = "Get all authors list",
		description = "Returns a list of all authors",
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
	public ResponseEntity<List<AuthorDto>> findAll();
	
	@GetMapping(path = "/paged")
	@Operation(
		summary = "Returns a page of authors",
		description = "Get all authors page",
		deprecated = true,
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
	public ResponseEntity<Page<AuthorDto>> findAllPaged(
		@Parameter(
			description = "Page index, size of elements per page and sorting",
			content = @Content(schema = @Schema(implementation = Pageable.class)))
		Pageable pageable
	);
	
	@GetMapping(value = "/{id}")
	@Operation(
		summary = "Returns a author by ID",
		description = "Get author details based on the ID provided",
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
	public ResponseEntity<AuthorDto> findById(
		@Parameter(description = "Author ID")
		@PathVariable Long id
	);
	
	@PostMapping
	@Operation(
			summary = "Creates a new author",
			description = "Creates a new author with provided data and then return created author details",
			security = @SecurityRequirement(name = "bearerAuth"),
			responses = {
				@ApiResponse(
					description = "Created",
					responseCode = "201",
					useReturnTypeSchema = true
				),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
				@ApiResponse(description = "Conflict", responseCode = "409", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
				@ApiResponse(description = "Unprocessable Entity", responseCode = "422", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
				@ApiResponse(description = "Internal error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
			}
		)
	ResponseEntity<AuthorDto> insert(
		@Parameter(
			description = "Payload with new author data to be inserted",
			content = @Content(schema = @Schema(implementation = AuthorDto.class))
		)
		@RequestBody @Valid AuthorDto dto
	);
	
	@PutMapping(value = "/{id}")
	@Operation(
		summary = "Updates author by ID",
		description = "Updates a author by ID and then return updated author data",
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
	ResponseEntity<AuthorDto> update(
		@Parameter(description = "Author ID")
		@PathVariable @Min(1) Long id,
		@Parameter(
			description = "Payload with new author data to be updated",
			content = @Content(schema = @Schema(implementation = AuthorDto.class))
		)
		@RequestBody @Valid AuthorDto dto
	);

}
