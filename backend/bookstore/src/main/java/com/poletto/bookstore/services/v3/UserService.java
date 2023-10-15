package com.poletto.bookstore.services.v3;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.controllers.v3.UserController;
import com.poletto.bookstore.converter.custom.UserMapper;
import com.poletto.bookstore.dto.v2.RoleDTOv2;
import com.poletto.bookstore.dto.v2.UserAuthDTOv2;
import com.poletto.bookstore.dto.v2.UserDTOv2;
import com.poletto.bookstore.entities.Reservation;
import com.poletto.bookstore.entities.User;
import com.poletto.bookstore.exceptions.DatabaseException;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.repositories.v2.RoleRepository;
import com.poletto.bookstore.repositories.v2.UserRepository;
import com.poletto.bookstore.util.CustomRedisClient;

import jakarta.persistence.EntityNotFoundException;

@Service("UserServiceV3")
public class UserService {

	//criar classes de teste e validar interação com outras entidades (reservation.client basicamente)
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private CustomRedisClient<String, Object> redisClient;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Cacheable("users")
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

	@Cacheable(value = "user", key = "#id")
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

	// TODO userAuth here??? frontend will eventually have person/user changes
	@Caching(
			evict = { @CacheEvict(value = {"users", "reservations", "reservation"}, allEntries = true) },
			put = { @CachePut(value = "user", key = "#id") }
	)
	@Transactional
	public UserDTOv2 update(Long id, UserAuthDTOv2 dto) {

			User entity = userRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Resource USER not found. ID: " + id));
			
			String previousEmail = entity.getEmail();
			
			List<Reservation> reservations = entity.getOrders();

			entity = UserMapper.convertDtoToEntityV2(dto);

			entity.setId(id);
			
			entity.setPassword(passwordEncoder.encode(dto.getPassword()));

			entity.getRoles().clear();

			for (RoleDTOv2 roleDTO : dto.getRoles()) {
				
				//TODO catch and throw?

				try {

					entity.getRoles().add(roleRepository.getReferenceById(roleDTO.getId()));

				} catch (EntityNotFoundException e) {

					throw new ResourceNotFoundException("Resource ROLE not found. ID " + roleDTO.getId());

				}
			}

			entity = userRepository.save(entity);

			if (redisClient.del("userAuth::" + previousEmail)) {
				logger.info("Cache userAuth evicted, e-mail: {}", previousEmail);
			}
			
			for (Reservation res: reservations) {
				res.setClient(entity);
				if (redisClient.put("reservation::" + res.getId(), res)) {
					logger.info("Cache reservation::{} updated client to: {}", res.getId(), res);
				}
			}
			
			UserDTOv2 newDto = UserMapper.convertEntityToDtoV2(entity);
			
			logger.info("Resource USER updated: {}", newDto);
			
			newDto.add(linkTo(methodOn(UserController.class).findById(newDto.getId())).withSelfRel().withType("GET"));
			newDto.add(linkTo(methodOn(UserController.class).delete(newDto.getId())).withRel("delete").withType("DELETE"));
			newDto.add(linkTo(methodOn(UserController.class).update(newDto.getId(), UserMapper.convertEntityToAuthDtoV2(UserMapper.convertDtoToEntityV2(newDto)))).withRel("update").withType("PUT"));

			return newDto;

	}

	@Caching(evict = { 
			@CacheEvict(value = "users", allEntries = true),
			@CacheEvict(value = "user", key = "#id")
	})
	@Transactional
	public void delete(Long id) {

		try {
			
			String email = userRepository.getReferenceById(id).getEmail();
			
			userRepository.deleteById(id);
			
			if (redisClient.del("userAuth::" + email)) {				
				logger.info("Cache userAuth evicted, id: {} ", id);		
			}
			
			logger.info("Resource USER deleted, id: {}", id);

		} catch (EntityNotFoundException e) {

			throw new ResourceNotFoundException("Resource USER not found. ID " + id);

		} catch (DataIntegrityViolationException e) {

			throw new DatabaseException("Integrity violation");

		}

	}

}
