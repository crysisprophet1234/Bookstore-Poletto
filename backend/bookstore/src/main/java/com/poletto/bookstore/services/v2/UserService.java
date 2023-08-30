package com.poletto.bookstore.services.v2;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.config.JwtService;
import com.poletto.bookstore.controllers.v2.UserController;
import com.poletto.bookstore.converter.custom.UserMapper;
import com.poletto.bookstore.dto.v2.RoleDTOv2;
import com.poletto.bookstore.dto.v2.UserAuthDTOv2;
import com.poletto.bookstore.dto.v2.UserDTOv2;
import com.poletto.bookstore.entities.User;
import com.poletto.bookstore.exceptions.DatabaseException;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.exceptions.UnauthorizedException;
import com.poletto.bookstore.repositories.v2.RoleRepository;
import com.poletto.bookstore.repositories.v2.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service("UserServiceV2")
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

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

	@Transactional(readOnly = true)
	public Page<UserDTOv2> findAllPaged(Pageable pageable) {

		Page<User> userPage = userRepository.findAll(pageable);

		logger.info("Resource USER page found: " + "PAGE NUMBER [" + userPage.getNumber() + "] - CONTENT: "
				+ userPage.getContent());
		
		Page<UserDTOv2> dtos = userPage.map(x -> UserMapper.convertEntityToDtoV2(x));
		
		dtos.stream().forEach(x -> x
				.add(linkTo(methodOn(UserController.class).findById(x.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(UserController.class).delete(x.getId())).withRel("delete").withType("DELETE"))
				.add(linkTo(methodOn(UserController.class).update(x.getId(), UserMapper.convertEntityToAuthDtoV2(UserMapper.convertDtoToEntityV2(x)))).withRel("update").withType("PUT")));

		return dtos;

	}

	@Transactional(readOnly = true)
	public UserDTOv2 findById(Long id) {

		Optional<User> user = userRepository.findById(id);

		User entity = user.orElseThrow(() -> new ResourceNotFoundException("Resource USER not found. ID " + id));

		logger.info("Resource USER found: " + entity.toString());
		
		UserDTOv2 dto = UserMapper.convertEntityToDtoV2(entity);
		
		dto.add(linkTo(methodOn(UserController.class).findById(dto.getId())).withSelfRel().withType("GET"));
		dto.add(linkTo(methodOn(UserController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
		dto.add(linkTo(methodOn(UserController.class).update(dto.getId(), UserMapper.convertEntityToAuthDtoV2(UserMapper.convertDtoToEntityV2(dto)))).withRel("update").withType("PUT"));

		return dto;

	}

	@Transactional
	public UserDTOv2 insert(UserAuthDTOv2 dto) {

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

		logger.info("Resource USER authenticated " + entity.toString());
		
		userAuthDTO.add(linkTo(methodOn(UserController.class).findById(userAuthDTO.getId())).withSelfRel().withType("GET"));
		userAuthDTO.add(linkTo(methodOn(UserController.class).delete(userAuthDTO.getId())).withRel("delete").withType("DELETE"));
		userAuthDTO.add(linkTo(methodOn(UserController.class).update(userAuthDTO.getId(), UserMapper.convertEntityToAuthDtoV2(UserMapper.convertDtoToEntityV2(userAuthDTO)))).withRel("update").withType("PUT"));

		return userAuthDTO;

	}

	// TODO userAuth here??? frontend will eventually have person/user changes
	
	@Transactional
	public UserDTOv2 update(Long id, UserAuthDTOv2 dto) {

		try {

			User entity = userRepository.getReferenceById(id);

			logger.info(entity.toString());

			entity = UserMapper.convertDtoToEntityV2(dto);

			entity.setId(id);
			
			entity.setPassword(passwordEncoder.encode(dto.getPassword()));

			entity.getRoles().clear();

			for (RoleDTOv2 roleDTO : dto.getRoles()) {

				try {

					entity.getRoles().add(roleRepository.getReferenceById(roleDTO.getId()));

				} catch (EntityNotFoundException e) {

					throw new ResourceNotFoundException("Resource ROLE not found. ID " + roleDTO.getId());

				}
			}

			entity = userRepository.save(entity);

			logger.info("Resource USER updated: " + entity.toString());

			UserDTOv2 newDto = UserMapper.convertEntityToDtoV2(entity);
			
			newDto.add(linkTo(methodOn(UserController.class).findById(newDto.getId())).withSelfRel().withType("GET"));
			newDto.add(linkTo(methodOn(UserController.class).delete(newDto.getId())).withRel("delete").withType("DELETE"));
			newDto.add(linkTo(methodOn(UserController.class).update(newDto.getId(), UserMapper.convertEntityToAuthDtoV2(UserMapper.convertDtoToEntityV2(newDto)))).withRel("update").withType("PUT"));

			return newDto;

		} catch (EntityNotFoundException e) {

			throw new ResourceNotFoundException("Resource USER not found. ID " + id);

		}

	}

	public void delete(Long id) {

		try {

			userRepository.deleteById(id);

		} catch (EmptyResultDataAccessException e) {

			throw new ResourceNotFoundException("Resource USER not found. ID " + id);

		} catch (DataIntegrityViolationException e) {

			throw new DatabaseException("Integrity violation");

		}

	}

}
