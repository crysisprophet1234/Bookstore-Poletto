package com.poletto.bookstore.controllers.v3;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poletto.bookstore.dto.v3.UserDto;
import com.poletto.bookstore.exceptions.exceptionresponse.ExceptionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RequestMapping(value = "/v3/auth")
@Tag(name = "Auth Controller V3", description = "Endpoints related to account creating and authentication")
@Validated
public interface AuthController {
	
	@PostMapping("/register")
	@Operation(
		summary = "Register a user new account",
		description = "Register a new user account with email and password provided and returns new user data",
		responses = {
			@ApiResponse(
				description = "Resource created",
				responseCode = "201",
				useReturnTypeSchema = true
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Unprocessable Entity", responseCode = "422", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Internal error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
		}
	)
	ResponseEntity<UserDto> insert(
		@Parameter(description = "New account data with email and password")
		@RequestBody @Valid UserDto userDTO
	);
	
	@Operation(
		summary = "Authenticates user",
		description = "Authenticates user with email and password then returns user data",
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "200",
				useReturnTypeSchema = true
			),
			@ApiResponse(description = "Bad credentials", responseCode = "401", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Internal error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
		}
	)
	@PostMapping("/authenticate")
	ResponseEntity<UserDto> authenticate(
		@Parameter(description = "Email and password for login")
		@RequestBody UserDto userDTO
	);

}
