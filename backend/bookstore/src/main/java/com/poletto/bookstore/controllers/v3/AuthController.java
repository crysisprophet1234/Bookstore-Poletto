package com.poletto.bookstore.controllers.v3;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poletto.bookstore.dto.v2.UserAuthDTOv2;
import com.poletto.bookstore.dto.v2.UserDTOv2;
import com.poletto.bookstore.exceptions.exceptionresponse.ExceptionResponse;
import com.poletto.bookstore.services.v2.EmailService;
import com.poletto.bookstore.services.v3.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController("AuthControllerV3")
@RequestMapping(value = "/auth/v3")
@Tag(name = "Auth Controller V3", description = "Endpoints related to account creating and authentication")
public class AuthController {

	@Autowired
	private AuthService authService;
	
	@Autowired
	private EmailService emailService;

	@PostMapping("/register")
	@Operation(
		summary = "Register a new account",
		description = "Register a new account with data provided and returns new user data",
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
	public ResponseEntity<UserDTOv2> insert(
			@Parameter(
				description = "New account data with firstname, lastname, e-mail and password",
				content = @Content(schema = @Schema(implementation = UserAuthDTOv2.class)))
			@RequestBody @Valid UserAuthDTOv2 userDTO
		) {
		UserDTOv2 dto = authService.register(userDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		emailService.sendEmailFromTemplate(dto.getEmail(), "Confirmação de criação de conta", dto.getFirstname() + " " + dto.getLastname());
		return ResponseEntity.created(uri).body(dto);
	}
	
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
	public ResponseEntity<UserAuthDTOv2> authenticate(
			@Parameter(
				description = "Email and password for login",
				content = @Content(schema = @Schema(example = "")))
			@RequestBody UserAuthDTOv2 userDTO
		) {
		UserAuthDTOv2 dto = authService.authenticate(userDTO);
		return ResponseEntity.ok().body(dto);
	}

}
