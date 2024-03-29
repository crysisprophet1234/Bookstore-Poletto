package com.poletto.bookstore.services.v1;

import java.util.List;
import java.util.Optional;

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
import com.poletto.bookstore.converter.custom.UserMapper;
import com.poletto.bookstore.dto.v1.RoleDTOv1;
import com.poletto.bookstore.dto.v1.UserAuthDTOv1;
import com.poletto.bookstore.dto.v1.UserDTOv1;
import com.poletto.bookstore.entities.User;
import com.poletto.bookstore.exceptions.DatabaseException;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.exceptions.UnauthorizedException;
import com.poletto.bookstore.repositories.v1.RoleRepository;
import com.poletto.bookstore.repositories.v1.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
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
	@Deprecated
	public List<UserDTOv1> findAll() {
		List<User> list = userRepository.findAll();
		return list.stream().map(x -> new UserDTOv1(x)).toList();
	}
	
	@Transactional(readOnly = true)
	public Page<UserDTOv1> findAllPaged(Pageable pageable) {
		
		Page<User> userPage = userRepository.findAll(pageable);
		
		logger.info("Resource USER page found: " + "PAGE NUMBER [" + userPage.getNumber() + "] - CONTENT: " + userPage.getContent());
		
		return userPage.map(x -> UserMapper.convertEntityToDto(x));
		
	}

	@Transactional(readOnly = true)
	public UserDTOv1 findById(Long id) {
		
		Optional<User> user = userRepository.findById(id);
		
		User entity = user.orElseThrow(() -> new ResourceNotFoundException("Resource USER not found. ID " + id));
		
		logger.info("Resource USER found: " + entity.toString());
		
		return UserMapper.convertEntityToDto(entity);

	}

	@Transactional
	public UserDTOv1 insert(UserAuthDTOv1 dto) {

		User entity = UserMapper.convertDtoToEntity(dto);
		
		entity.getRoles().add(roleRepository.getReferenceById(1L));

		entity.setPassword(passwordEncoder.encode(dto.getPassword()));

		entity = userRepository.save(entity);
		
		logger.info("Resource USER saved: " + entity.toString());

		return UserMapper.convertEntityToDto(entity);

	}
	
	@Transactional (readOnly = true)
	public UserAuthDTOv1 authenticate(UserAuthDTOv1 dto) {
		
		try {
			
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
		
		} catch (BadCredentialsException e) {
			throw new UnauthorizedException("Bad credentials for login");
		}

		User entity = userRepository.findByEmail(dto.getEmail()).orElseThrow();
		
		UserAuthDTOv1 userAuthDTO = UserMapper.convertEntityToAuthDto(entity);

		String jwtToken = jwtService.generateToken(entity);

		userAuthDTO.setToken(jwtToken);
		
		logger.info("Resource USER authenticated " + entity.toString());
		
		return userAuthDTO;

	}

	@Transactional
	public UserDTOv1 update(Long id, UserAuthDTOv1 dto) {

		try {

			User entity = userRepository.getReferenceById(id);

			entity = UserMapper.convertDtoToEntity(dto);
			
			entity.setId(id);
			
			entity.getRoles().clear();
			
			for (RoleDTOv1 roleDTO : dto.getRoles()) {
				entity.getRoles().add(roleRepository.getReferenceById(roleDTO.getId()));
			}

			entity = userRepository.save(entity);
			
			logger.info("Resource USER updated: " + entity.toString());

			return UserMapper.convertEntityToDto(entity);

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
