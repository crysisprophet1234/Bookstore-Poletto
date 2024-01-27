package com.poletto.bookstore.controllers.v3;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poletto.bookstore.dto.v3.RoleDto;
import com.poletto.bookstore.dto.v3.UserUpdateDto;
import com.poletto.bookstore.dto.v3.UserDto;
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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@RequestMapping(value = "/v3/users")
@Tag(name = "User Controller V3", description = "Endpoints related to user resources management")
@SecurityRequirement(name = "bearerAuth")
@Validated
public interface UserController {
	
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
	ResponseEntity<Page<UserDto>> findAllPaged(
			@Parameter(description = "Page index")
			@RequestParam(value = "page", defaultValue = "0")
			Integer page,
			
			@Parameter(description = "Page size")
			@RequestParam(value = "size", defaultValue = "12")
			Integer size,
			
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
				regexp = "^(id|email|accountStatus|userStatus)$",
				message = "Parâmetro orderBy deve ser id, email, accountStatus ou userStatus"
			)
			String orderBy,
			
			@Parameter(description = "User status")
			@RequestParam(value = "userStatus", defaultValue = "all")	
			@Pattern(
				regexp = "^(?i)(ACTIVE|INACTIVE|SUSPENDED|ALL)$",
				message = "Parâmetro userStatus deve ser ACTIVE, INACTIVE, SUSPENDED ou ALL"
			)
			String userStatus,
			
			@Parameter(description = "Account status")
			@RequestParam(value = "accountStatus", defaultValue = "all")	
			@Pattern(
				regexp = "^(?i)(VERIFIED|UNVERIFIED|ALL)$",
				message = "Parâmetro accountStatus deve ser VERIFIED, UNVERIFIED ou ALL"
			)
			String accountStatus,
			
			@Parameter(description = "Role id")
			@RequestParam(value = "roleId", required = false)	
			@Min(1)
			Long roleId
	);
	
	@GetMapping(value = "/{userId}")
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
	ResponseEntity<UserDto> findById(
		@Parameter(description = "User ID")
		@PathVariable @Min(1) Long userId
	);
	
	@PutMapping(value = "/{userId}/change-email")
	@Operation(
		summary = "Updates a user e-mail by ID",
		description = "Updates a user e-mail by ID and then return updated user data",
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
	ResponseEntity<UserDto> updateEmail(
		@Parameter(description = "User ID")
		@PathVariable @Min(1) Long userId,
		@Parameter(
			description = "New user e-mail",
			content = @Content(schema = @Schema(name = "email", defaultValue = "NewEmail@host.com"))
		)
		@RequestBody @Valid UserUpdateDto userChangesDto,
		@Parameter(description = "Authorization header token") 
		@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization
	);
	
	@PutMapping(value = "/{userId}/change-password")
	@Operation(
		summary = "Updates a user password by ID",
		description = "Updates a user password by ID and then return updated user data",
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
	ResponseEntity<UserDto> updatePassword(
		@Parameter(description = "User ID")
		@PathVariable @Min(1) Long userId,
		@Parameter(
			description = "New user password",
			content = @Content(schema = @Schema(name = "password", implementation = UserUpdateDto.class))
		)
		@RequestBody @Valid UserUpdateDto userChangesDto,
		@Parameter(description = "Authorization header token") 
		@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization
	);
	
	@PutMapping(value = "/{userId}/add-roles")
	@Operation(
		summary = "Add one or more roles to the user by ID",
		description = "Add one or more roles by user ID and then return updated user data",
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
	ResponseEntity<UserDto> addUserRoles(
		@Parameter(description = "User ID")
		@PathVariable @Min(1) Long userId,
		@Parameter(
			description = "Roles to be added for the user",
			content = @Content(schema = @Schema(name = "roles list", implementation = RoleDto.class))
		)
		@RequestBody @Valid @NotEmpty(message = "Lista de roles a serem adicionadas ao usuário não pode estar vazia") Set<RoleDto> roles
	);
	
	@PutMapping(value = "/{userId}/remove-roles")
	@Operation(
		summary = "Remove one or more roles from the user by ID",
		description = "Remove one or more roles from the user by ID and then return updated user data",
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
	ResponseEntity<UserDto> removeUserRoles(
		@Parameter(description = "User ID")
		@PathVariable @Min(1) Long userId,
		@Parameter(
			description = "Roles to be remove from the user",
			content = @Content(schema = @Schema(name = "roles list", implementation = RoleDto.class))
		)
		@RequestBody @Valid @NotEmpty(message = "Lista de roles a serem removidas do usuário não pode estar vazia") Set<RoleDto> roles
	);

	@PutMapping(value = "/{userId}/change-status")
	@Operation(
		summary = "Updates a user status by ID",
		description = "Updates a user status by ID and then return updated user data",
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
	ResponseEntity<UserDto> updateUserStatus(
		@Parameter(description = "User ID")
		@PathVariable @Min(1) Long userId,
		@Parameter(
			description = "New user status",
			content = @Content(schema = @Schema(name = "status", defaultValue = "ACTIVE"))
		)
		@RequestBody @Valid UserUpdateDto userChangesDto
	);
	
	@GetMapping(value = "/{userId}/send-verification-email")
	@Operation(
		summary = "Sends a verification e-mail to the user by ID",
		description = "Sends a verification e-mail to the user by ID, using his registered email and then returns a 204",
		responses = {
			@ApiResponse(
				description = "E-mail sent",
				responseCode = "204",
				useReturnTypeSchema = true
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Internal error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
		}
	)
	ResponseEntity<Void> sendAccountVerificationEmail(
		@Parameter(description = "User ID")	
		@PathVariable @Min(1) Long userId
	);
	
	@PutMapping(value = "/verify-account")
	@Operation(
		summary = "Verify a user account using token",
		description = "Verify a user account using token an returns a 204",
		responses = {
			@ApiResponse(
				description = "Account verified",
				responseCode = "204",
				useReturnTypeSchema = true
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Internal error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
		}
	)
	ResponseEntity<Void> verifyUserAccount(
		@Parameter(description = "Verification Token UUID for user account verification")
		@RequestParam(value = "verificationToken", required = true) UUID verificationToken
	);

}
