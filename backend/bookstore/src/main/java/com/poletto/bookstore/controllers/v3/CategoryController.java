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

import com.poletto.bookstore.dto.v2.CategoryDTOv2;
import com.poletto.bookstore.exceptions.exceptionresponse.ExceptionResponse;
import com.poletto.bookstore.services.v3.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController("CategoryControllerV3")
@RequestMapping (value = "/categories/v3")
@Tag(name = "Category Controller V3", description = "Endpoints related to category resources management")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
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
	public ResponseEntity<List<CategoryDTOv2>> findAll() {
		List<CategoryDTOv2> categories = categoryService.findAll();
		return ResponseEntity.ok().body(categories);
	}
	
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
	public ResponseEntity<Page<CategoryDTOv2>> findAllPaged(
			@Parameter(
					description = "Page index, size of elements per page and sorting",
					content = @Content(schema = @Schema(implementation = Pageable.class)))
			Pageable pageable
		) {
		Page<CategoryDTOv2> list = categoryService.findAllPaged(pageable);		
		return ResponseEntity.ok().body(list);
	}
	
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
	public ResponseEntity<CategoryDTOv2> findById(
			@Parameter(description = "Category ID")
			@PathVariable Long id
		) {
		CategoryDTOv2 dto = categoryService.findById(id);
		return ResponseEntity.ok().body(dto);
	}

}
