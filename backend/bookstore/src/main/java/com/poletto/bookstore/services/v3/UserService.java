package com.poletto.bookstore.services.v3;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.repositories.v3.RoleRepository;
import com.poletto.bookstore.repositories.v3.UserRepository;
import com.poletto.bookstore.util.CustomRedisClient;

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

	@Transactional(readOnly = true)
	public Page<UserDTOv2> findAllPaged(Pageable pageable) {

		Page<User> entitiesPage = userRepository.findAll(pageable);

		logger.info("Resource USER page found: " + "PAGE NUMBER [" + entitiesPage.getNumber() + "] - CONTENT: "
				+ entitiesPage.getContent());
		
		Page<UserDTOv2> dtosPage = entitiesPage.map(x -> UserMapper.convertEntityToDtoV2(x));
		
		dtosPage.stream().forEach(dto -> dto
				.add(linkTo(methodOn(UserController.class).findById(dto.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(UserController.class).delete(dto.getId())).withRel("delete").withType("DELETE"))
				.add(linkTo(methodOn(UserController.class).update(dto.getId(), UserMapper.convertEntityToAuthDtoV2(UserMapper.convertDtoToEntityV2(dto)))).withRel("update").withType("PUT")));

		return dtosPage;

	}

	@Transactional(readOnly = true)
	public UserDTOv2 findById(Long id) {

		User entity = userRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Resource USER not found. ID " + id));

		logger.info("Resource USER found: " + entity.toString());
		
		return UserMapper.convertEntityToDtoV2(entity)
			.add(linkTo(methodOn(UserController.class).findById(entity.getId())).withSelfRel().withType("GET"))
			.add(linkTo(methodOn(UserController.class).delete(entity.getId())).withRel("delete").withType("DELETE"))
			.add(linkTo(methodOn(UserController.class).update(entity.getId(), UserMapper.convertEntityToAuthDtoV2(entity))).withRel("update").withType("PUT"));

	}

	// TODO userAuth here??? frontend will eventually have person/user changes
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

				entity.getRoles().add(roleRepository.getReferenceById(roleDTO.getId()));

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
			
			logger.info("Resource USER updated: {}", entity);
			
			return UserMapper.convertEntityToDtoV2(entity)
				.add(linkTo(methodOn(UserController.class).findById(entity.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(UserController.class).delete(entity.getId())).withRel("delete").withType("DELETE"))
				.add(linkTo(methodOn(UserController.class).update(entity.getId(), UserMapper.convertEntityToAuthDtoV2(entity))).withRel("update").withType("PUT"));

	}

	@Transactional
	public void delete(Long id) {

		String email = userRepository.getReferenceById(id).getEmail();
		
		userRepository.deleteById(id);
		
		if (redisClient.del("userAuth::" + email)) {				
			logger.info("Cache userAuth evicted, id: {} ", id);		
		}
		
		logger.info("Resource USER deleted, id: {}", id);

	}

}
