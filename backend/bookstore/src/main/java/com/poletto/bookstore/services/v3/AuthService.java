package com.poletto.bookstore.services.v3;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
import com.poletto.bookstore.exceptions.UnauthorizedException;
import com.poletto.bookstore.repositories.v2.RoleRepository;
import com.poletto.bookstore.repositories.v2.UserRepository;

@Service("AuthServiceV3")
public class AuthService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@CacheEvict(value = "users", allEntries = true)
	@Transactional
	public UserDTOv2 register(UserAuthDTOv2 dto) {

		User entity = UserMapper.convertDtoToEntityV2(dto);

		entity.getRoles().add(roleRepository.getReferenceById(1L));

		entity.setPassword(passwordEncoder.encode(dto.getPassword()));

		entity = userRepository.save(entity);

		logger.info("Resource USER saved: " + entity.toString());

		UserDTOv2 newDto = UserMapper.convertEntityToDtoV2(entity);
		
		newDto.add(linkTo(methodOn(UserController.class).findById(newDto.getId())).withSelfRel().withType("GET"));
		newDto.add(linkTo(methodOn(UserController.class).delete(newDto.getId())).withRel("delete").withType("DELETE"));
		newDto.add(linkTo(methodOn(UserController.class).update(newDto.getId(), UserMapper.convertEntityToAuthDtoV2(UserMapper.convertDtoToEntityV2(newDto)))).withRel("update").withType("PUT"));

		return newDto;

	}
	
	@Cacheable(value = "userAuth", key = "#dto.email")
	@Transactional(readOnly = true)
	public UserAuthDTOv2 authenticate(UserAuthDTOv2 dto) {

		try {

			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

		} catch (BadCredentialsException e) {
			throw new UnauthorizedException("Bad credentials for login");
		}

		User entity = userRepository.findByEmail(dto.getEmail()).orElseThrow();

		UserAuthDTOv2 userAuthDTO = UserMapper.convertEntityToAuthDtoV2(entity);

		String jwtToken = jwtService.generateToken(entity);

		userAuthDTO.setToken(jwtToken);

		logger.info("Resource USER authenticated {}", userAuthDTO);
		
		userAuthDTO.add(linkTo(methodOn(UserController.class).findById(userAuthDTO.getId())).withSelfRel().withType("GET"));
		userAuthDTO.add(linkTo(methodOn(UserController.class).delete(userAuthDTO.getId())).withRel("delete").withType("DELETE"));
		userAuthDTO.add(linkTo(methodOn(UserController.class).update(userAuthDTO.getId(), UserMapper.convertEntityToAuthDtoV2(UserMapper.convertDtoToEntityV2(userAuthDTO)))).withRel("update").withType("PUT"));

		return userAuthDTO;

	}

}
