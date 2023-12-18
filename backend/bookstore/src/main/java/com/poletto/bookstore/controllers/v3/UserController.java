package com.poletto.bookstore.controllers.v3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poletto.bookstore.dto.v2.UserDTOv2;
import com.poletto.bookstore.exceptions.exceptionresponse.ExceptionResponse;
import com.poletto.bookstore.services.v3.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController("UserControllerV3")
@RequestMapping(value = "/users/v3")
@Tag(name = "User Controller V3", description = "Endpoints related to user resources management")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping
	@Operation(
		summary = "Returns a page of users",
		description = "Get users page filtered by the parameters provided",
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
	public ResponseEntity<Page<UserDTOv2>> findAllPaged(
			@Parameter(
				description = "Page index, size of elements per page and sorting",
				content = @Content(schema = @Schema(implementation = Pageable.class)))
			Pageable pageable
		) {
		Page<UserDTOv2> list = userService.findAllPaged(pageable);
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	@Operation(
		summary = "Returns a user by ID",
		description = "Get user details based on the ID provided",
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
	public ResponseEntity<UserDTOv2> findById(
			@Parameter(description = "User ID")
			@PathVariable Long id
		) {
		UserDTOv2 userDTO = userService.findById(id);
		return ResponseEntity.ok().body(userDTO);
	}
	
	@PutMapping(value = "/{id}")
	@Operation(
		summary = "Updates a user by ID",
		description = "Updates a user by ID and then return updated data",
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
	public ResponseEntity<UserDTOv2> update(
			@Parameter(description = "ID of the user")
			@PathVariable Long id,
			@Parameter(
				description = "Payload with new user data to be updated",
				content = @Content(schema = @Schema(implementation = UserDTOv2.class))) 
			@RequestBody @Valid UserDTOv2 dto
		) {
		UserDTOv2 userDTO = userService.update(id, dto);
		return ResponseEntity.ok().body(userDTO);
	}
	
	@DeleteMapping(value = "/{id}")
	@Operation(
		summary = "Deletes a user by ID",
		description = "Deletes user based on the ID provided",
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "204"
			),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Internal error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
		}
	)
	@ApiResponse(responseCode = "204", description = "User deleted successfully")
	public ResponseEntity<Void> delete(
			@Parameter(description = "ID of the user")
			@PathVariable Long id
		) {
		userService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
