package com.poletto.bookstore.controllers.v3;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poletto.bookstore.dto.v3.AddressDto;
import com.poletto.bookstore.dto.v3.PersonDto;
import com.poletto.bookstore.dto.v3.PersonUpdateDto;
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

@RequestMapping(value = "/v3/persons")
@Tag(name = "Person Controller V3", description = "Endpoints related to person")
@SecurityRequirement(name = "bearerAuth")
@Validated
public interface PersonController {
	
	@GetMapping(value = "/{personId}")
	@Operation(
		summary = "Returns a person by ID",
		description = "Get person details based on the ID provided",
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
	ResponseEntity<PersonDto> findById(
		@Parameter(description = "Person ID")
		@PathVariable @Min(1) Long personId
	);
	
	@PostMapping(value = "/create")
	@Operation(
		summary = "Creates a person entity",
		description = "Creates a person entity with provided data and respective user ID",
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
	ResponseEntity<PersonDto> create(
		@Parameter(
			description = "New person data",
			content = @Content(schema = @Schema(name = "Person Dto", implementation = PersonDto.class))
		)
		@RequestBody @Valid PersonDto personDto
	);
	
	@PutMapping(value = "/{personId}/update")
	@Operation(
		summary = "Updates a person entity by ID",
		description = "Updates a person entity with provided data and ID",
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "200",
				useReturnTypeSchema = true
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Conflict", responseCode = "409", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Unprocessable Entity", responseCode = "422", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Internal error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
		}
	)
	ResponseEntity<PersonDto> update(
		@Parameter(description = "Person ID")
		@PathVariable @Min(1) Long personId,
		@Parameter(
			description = "Person updated data",
			content = @Content(schema = @Schema(name = "Person update Dto", implementation = PersonUpdateDto.class))
		)
		@RequestBody @Valid PersonUpdateDto personUpdateDto
	);
	
	@PostMapping(value = "/{personId}/addresses/create")
	@Operation(
		summary = "Creates a address",
		description = "Creates a address an atacches it to a person by ID",
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
	ResponseEntity<AddressDto> addAddress(
		@Parameter(description = "Person ID")
		@PathVariable @Min(1) Long personId,
		@Parameter(
			description = "New address dto",
			content = @Content(schema = @Schema(name = "New address dto", implementation = AddressDto.class))
		)
		@RequestBody @Valid AddressDto addressDto
	);
	
	@PutMapping(value = "/{personId}/addresses/{addressId}/update")
	@Operation(
		summary = "Updates a address",
		description = "Updates a address based on address ID",
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
	ResponseEntity<AddressDto> updateAddress(
		@Parameter(description = "Person ID")
		@PathVariable @Min(1) Long personId,
		@Parameter(description = "Address ID")
		@PathVariable @Min(1) Long addressId,
		@Parameter(
			description = "Updated address dto",
			content = @Content(schema = @Schema(name = "Updated address dto", implementation = AddressDto.class))
		)
		@RequestBody @Valid AddressDto addressDto
	);
	

}
