package com.poletto.bookstore.services.v3.impl;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.config.JwtService;
import com.poletto.bookstore.controllers.v3.UserController;
import com.poletto.bookstore.converter.v3.UserMapperV3;
import com.poletto.bookstore.dto.v3.UserDto;
import com.poletto.bookstore.entities.User;
import com.poletto.bookstore.entities.enums.AccountStatus;
import com.poletto.bookstore.entities.enums.UserStatus;
import com.poletto.bookstore.exceptions.AlreadyExistingAccountException;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.exceptions.UnauthorizedException;
import com.poletto.bookstore.repositories.v3.AuthRepository;
import com.poletto.bookstore.repositories.v3.RoleRepository;
import com.poletto.bookstore.services.v3.AuthService;

@Service("AuthServiceV3")
public class AuthServiceImpl implements AuthService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
	
	@Autowired
	private AuthRepository authRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	@Transactional
	public UserDto register(UserDto dto) {

		User userEntity = UserMapperV3.INSTANCE.userDtoToUser(dto);
		
		if (authRepository.existsByEmail(dto.getEmail())) {
			throw new AlreadyExistingAccountException(dto.getEmail());
		}

		userEntity.getRoles().add(roleRepository.getReferenceById(1L));
		
		userEntity.setAccountStatus(AccountStatus.UNVERIFIED);
		
		userEntity.setUserStatus(UserStatus.ACTIVE);

		userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
		
		userEntity = authRepository.save(userEntity);

		logger.info("Resource USER saved: " + userEntity.toString());

		return userDtoWithLinks(UserMapperV3.INSTANCE.userToUserDto(userEntity));

	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDto authenticate(UserDto dto) {

		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
		);

		User userEntity = authRepository.findByEmail(dto.getEmail()).orElseThrow(
				() -> new ResourceNotFoundException("Resource USER not found. Email " + dto.getEmail()));
		
		if (userEntity.getUserStatus() != UserStatus.ACTIVE) {
			throw new UnauthorizedException("User account " + dto.getEmail() + " is actually " + userEntity.getUserStatus());
		}
		
		UserDto authenticatedUserDto = UserMapperV3.INSTANCE.userToUserDto(userEntity);

		String jwtToken = jwtService.generateToken(userEntity);

		authenticatedUserDto.setToken(jwtToken);

		logger.info("Resource USER authenticated {}", authenticatedUserDto);

		return userDtoWithLinks(authenticatedUserDto);

	}
	
	private UserDto userDtoWithLinks(UserDto userDto) {
		return userDto
				.add(linkTo(methodOn(UserController.class)
						.findById(userDto.getKey())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(UserController.class)
						.updateEmail(userDto.getKey(), null, null)).withRel("update email").withType("PUT"))
				.add(linkTo(methodOn(UserController.class)
						.updateUserStatus(userDto.getKey(), null)).withRel("update userStatus").withType("PUT"))
				.add(linkTo(methodOn(UserController.class)
						.updatePassword(userDto.getKey(), null, null)).withRel("update password").withType("PUT"));
	}

}
