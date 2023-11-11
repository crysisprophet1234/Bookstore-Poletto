package com.poletto.bookstore.services.v3;

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
import com.poletto.bookstore.converter.custom.UserMapper;
import com.poletto.bookstore.dto.v2.UserAuthDTOv2;
import com.poletto.bookstore.dto.v2.UserDTOv2;
import com.poletto.bookstore.entities.User;
import com.poletto.bookstore.exceptions.AlreadyExistingAccountException;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.repositories.v3.AuthRepository;
import com.poletto.bookstore.repositories.v3.RoleRepository;

@Service("AuthServiceV3")
public class AuthService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	
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
	
	@Transactional
	public UserDTOv2 register(UserAuthDTOv2 dto) {

		User entity = UserMapper.convertDtoToEntityV2(dto);
		
		if (authRepository.existsByEmail(dto.getEmail())) {
			throw new AlreadyExistingAccountException(dto.getEmail());
		}

		entity.getRoles().add(roleRepository.getReferenceById(1L));

		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		
		entity = authRepository.save(entity);

		logger.info("Resource USER saved: " + entity.toString());

		return UserMapper.convertEntityToDtoV2(entity)
				.add(linkTo(methodOn(UserController.class).findById(entity.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(UserController.class).delete(entity.getId())).withRel("delete").withType("DELETE"))
				.add(linkTo(methodOn(UserController.class).update(entity.getId(), UserMapper.convertEntityToAuthDtoV2(entity))).withRel("update").withType("PUT"));

	}
	
	@Transactional(readOnly = true)
	public UserAuthDTOv2 authenticate(UserAuthDTOv2 dto) {

		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				dto.getEmail(),
				dto.getPassword()
		));

		User entity = authRepository.findByEmail(dto.getEmail()).orElseThrow(
				() -> new ResourceNotFoundException("Resource USER not found. Email " + dto.getEmail()));

		UserAuthDTOv2 userAuthDTO = UserMapper.convertEntityToAuthDtoV2(entity);

		String jwtToken = jwtService.generateToken(entity);

		userAuthDTO.setToken(jwtToken);

		logger.info("Resource USER authenticated {}", userAuthDTO);

		 userAuthDTO
				.add(linkTo(methodOn(UserController.class).findById(entity.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(UserController.class).delete(entity.getId())).withRel("delete").withType("DELETE"))
				.add(linkTo(methodOn(UserController.class).update(entity.getId(), UserMapper.convertEntityToAuthDtoV2(entity))).withRel("update").withType("PUT"));
		 
		 return userAuthDTO;

	}

}
