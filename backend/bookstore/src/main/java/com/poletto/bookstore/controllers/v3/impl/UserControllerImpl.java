package com.poletto.bookstore.controllers.v3.impl;

import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poletto.bookstore.controllers.v3.UserController;
import com.poletto.bookstore.dto.v3.RoleDto;
import com.poletto.bookstore.dto.v3.UserChangesDto;
import com.poletto.bookstore.dto.v3.UserDto;
import com.poletto.bookstore.services.v3.UserService;

@RestController("UserControllerV3")
@RequestMapping(value = "/v3/users")
public class UserControllerImpl implements UserController {

	@Autowired
	private UserService userService;
	
	@Override
	@GetMapping
	public ResponseEntity<Page<UserDto>> findAllPaged(
		@RequestParam(value = "page", defaultValue = "0") Integer page,
		@RequestParam(value = "size", defaultValue = "12") Integer size,
		@RequestParam(value = "sort", defaultValue = "asc") String sort,
		@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
		@RequestParam(value = "userStatus", defaultValue = "all") String userStatus,
		@RequestParam(value = "accountStatus", defaultValue = "all") String accountStatus,
		@RequestParam(value = "roleId", required = false) Long roleId
	) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.valueOf(sort.toUpperCase()), orderBy));
		
		Page<UserDto> userDtoPage = userService.findAllPaged(pageable, userStatus, accountStatus, roleId);
		
		return ResponseEntity.ok(userDtoPage);
	}

	@Override
	@GetMapping(value = "/{userId}")
	public ResponseEntity<UserDto> findById(@PathVariable Long userId) {
		
		UserDto userDto = userService.findById(userId);
		
		return ResponseEntity.ok().body(userDto);
		
	}
	
	@Override
	@PutMapping(value = "/{userId}/change-email")
	public ResponseEntity<UserDto> updateEmail(
			@PathVariable Long userId,
			@RequestBody UserChangesDto userChangesDto,
			@RequestHeader(value =  HttpHeaders.AUTHORIZATION) String authHeader
		) {
		
		userChangesDto.setToken(authHeader);
		
		UserDto updatedUserDto = userService.updateEmail(userId, userChangesDto);
		
		return ResponseEntity.ok().body(updatedUserDto);
		
	}
	
	@Override
	@PutMapping(value = "/{userId}/change-password")
	public ResponseEntity<UserDto> updatePassword(
			@PathVariable Long userId,
			@RequestBody UserChangesDto userChangesDto,
			@RequestHeader(value =  HttpHeaders.AUTHORIZATION) String authHeader
		) {
		
		userChangesDto.setToken(authHeader);
		
		UserDto updatedUserDto = userService.updatePassword(userId, userChangesDto);
		
		return ResponseEntity.ok().body(updatedUserDto);
		
	}

	@Override
	@PutMapping(value = "/{userId}/add-roles")
	public ResponseEntity<UserDto> addUserRoles(@PathVariable Long userId, @RequestBody Set<RoleDto> roles) {
		
		UserDto updatedUserDto = userService.addUserRoles(userId, roles);
		
		return ResponseEntity.ok().body(updatedUserDto);
		
	}

	@Override
	@PutMapping(value = "/{userId}/remove-roles")
	public ResponseEntity<UserDto> removeUserRoles(@PathVariable Long userId, @RequestBody Set<RoleDto> roles) {
		
		UserDto updatedUserDto = userService.removeUserRoles(userId, roles);
		
		return ResponseEntity.ok().body(updatedUserDto);
		
	}

	@Override
	@PutMapping(value = "/{userId}/change-status")
	public ResponseEntity<UserDto> updateUserStatus(@PathVariable Long userId, @RequestBody UserChangesDto userChangesDto) {
		
		UserDto updatedUserDto = userService.updateUserStatus(userId, userChangesDto);
		
		return ResponseEntity.ok().body(updatedUserDto);
	}
	
	@Override
	@GetMapping(value = "/{userId}/send-verification-email")
	public ResponseEntity<Void> sendAccountVerificationEmail(@PathVariable Long userId) {
		
		userService.sendAccountVerificationEmail(userId);
		
		return ResponseEntity.noContent().build();
	}	

	@Override
	@PutMapping(value = "/verify-account")
	public ResponseEntity<Void> verifyUserAccount(@RequestParam(value = "verificationToken", required = true) UUID verificationToken) {
		
		userService.verifyAccount(verificationToken);
		
		return ResponseEntity.noContent().build();
	}	

}
