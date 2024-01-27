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

import com.poletto.bookstore.dto.v3.CategoryDto;
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

@RequestMapping (value = "/categories/v3")
@Tag(name = "Category Controller V3", description = "Endpoints related to category resources management")
@Validated
public interface CategoryController {

	//TODO: add constraint validations	
	
	@GetMapping
	@Operation(
		summary = "Get all categories list",
		description = "Returns a list of all categories",
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
	ResponseEntity<List<CategoryDto>> findAll();
	
	@GetMapping(value = "/paged")
	@Operation(
		summary = "Get all categories page",
		description = "Returns a page of all categories",
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
	ResponseEntity<Page<CategoryDto>> findAllPaged(
		@Parameter(
				description = "Page index, size of elements per page and sorting",
				content = @Content(schema = @Schema(implementation = Pageable.class)))
		Pageable pageable
	);
	
	@GetMapping(value = "/{id}")
	@Operation(
		summary = "Returns a category by ID",
		description = "Get category details based on the ID provided",
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
	ResponseEntity<CategoryDto> findById(
		@Parameter(description = "Category ID")
		@PathVariable Long id
	);
	
	@PostMapping
	@Operation(
			summary = "Creates a new category",
			description = "Creates a new category with provided data and then return created category details",
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
	ResponseEntity<CategoryDto> insert(
		@Parameter(
			description = "Payload with new category data to be inserted",
			content = @Content(schema = @Schema(implementation = CategoryDto.class))
		)
		@RequestBody @Valid CategoryDto dto
	);
	
	@PutMapping(value = "/{id}")
	@Operation(
		summary = "Updates category by ID",
		description = "Updates a category by ID and then return updated category data",
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
	ResponseEntity<CategoryDto> update(
		@Parameter(description = "Category ID")
		@PathVariable @Min(1) Long id,
		@Parameter(
			description = "Payload with new category data to be updated",
			content = @Content(schema = @Schema(implementation = CategoryDto.class))
		)
		@RequestBody @Valid CategoryDto dto
	);
	

}
