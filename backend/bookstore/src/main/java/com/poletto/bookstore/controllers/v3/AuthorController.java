package com.poletto.bookstore.controllers.v3;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poletto.bookstore.dto.v2.AuthorDTOv2;
import com.poletto.bookstore.exceptions.exceptionresponse.ExceptionResponse;
import com.poletto.bookstore.services.v3.AuthorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController("AuthorControllerV3")
@RequestMapping("/authors/v3")
@Tag(name = "Author Controller V3", description = "Endpoints related to author resources management")
public class AuthorController {
	
	@Autowired
	private AuthorService authorService;
	
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
	public ResponseEntity<List<AuthorDTOv2>> findAll() {
		List<AuthorDTOv2> list = authorService.findAll();		
		return ResponseEntity.ok().body(list);
	}
	
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
	public ResponseEntity<Page<AuthorDTOv2>> findAllPaged(
			@Parameter(
				description = "Page index, size of elements per page and sorting",
				content = @Content(schema = @Schema(implementation = Pageable.class)))
			Pageable pageable
		) {
		Page<AuthorDTOv2> list = authorService.findAllPaged(pageable);		
		return ResponseEntity.ok().body(list);
	}
	
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
	public ResponseEntity<AuthorDTOv2> findById(
			@Parameter(description = "Author ID")
			@PathVariable Long id
		) {
		AuthorDTOv2 dto = authorService.findById(id);
		return ResponseEntity.ok().body(dto);
	}

}
