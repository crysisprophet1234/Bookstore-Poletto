package com.poletto.bookstore.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.config.JwtService;
import com.poletto.bookstore.converter.DozerMapperConverter;
import com.poletto.bookstore.converter.custom.UserMapper;
import com.poletto.bookstore.dto.RoleDTO;
import com.poletto.bookstore.dto.UserAuthDTO;
import com.poletto.bookstore.dto.UserDTO;
import com.poletto.bookstore.entities.Role;
import com.poletto.bookstore.entities.User;
import com.poletto.bookstore.repositories.RoleRepository;
import com.poletto.bookstore.repositories.UserRepository;
import com.poletto.bookstore.services.exceptions.DatabaseException;
import com.poletto.bookstore.services.exceptions.ResourceNotFoundException;

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
	public List<UserDTO> findAll() {
		List<User> list = userRepository.findAll();
		return list.stream().map(x -> new UserDTO(x)).toList();
	}
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> list = userRepository.findAll(pageable);
		return list.map(x -> UserMapper.convertEntityToDto(x));
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> user = userRepository.findById(id);
		User entity = user.orElseThrow(() -> new ResourceNotFoundException(id));
		
		return UserMapper.convertEntityToDto(entity);

	}

	@Transactional
	public UserDTO insert(UserAuthDTO dto) {

		User entity = UserMapper.convertDtoToEntity(dto);
		
		entity.getRoles().add(roleRepository.getReferenceById(1L));

		entity.setPassword(passwordEncoder.encode(dto.getPassword()));

		entity = userRepository.save(entity);
		
		logger.info("CREATED " + entity.toString());

		return UserMapper.convertEntityToDto(entity);

	}
	
	@Transactional (readOnly = true)
	public UserAuthDTO authenticate(UserAuthDTO dto) { // TODO verificar dados presentes na response

		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

		User entity = userRepository.findByEmail(dto.getEmail()).orElseThrow();
		
		UserAuthDTO userAuthDTO = UserMapper.convertEntityToAuthDto(entity);

		String jwtToken = jwtService.generateToken(entity);

		userAuthDTO.setToken(jwtToken);
		
		logger.info("AUTHENTICATED " + userAuthDTO.toString());
		
		return userAuthDTO;

	}

	@Transactional
	public UserDTO update(Long id, UserAuthDTO dto) {

		try {

			User entity = userRepository.getReferenceById(id);

			entity = UserMapper.convertDtoToEntity(dto);
			
			entity.setId(id);
			
			entity.getRoles().clear();
			
			for (RoleDTO roleDTO : dto.getRoles()) {
				entity.getRoles().add(roleRepository.getReferenceById(roleDTO.getId()));
			}

			entity = userRepository.save(entity);
			
			logger.info("UPDATED " + entity.toString());

			return UserMapper.convertEntityToDto(entity);

		} catch (EntityNotFoundException e) {

			throw new ResourceNotFoundException(id);

		}

	}

	public void delete(Long id) {
		
		try {
			
			userRepository.deleteById(id);
			
		} catch (EmptyResultDataAccessException e) {
			
			throw new ResourceNotFoundException(id);
			
		} catch (DataIntegrityViolationException e) {
			
			throw new DatabaseException("Integrity violation");
			
		}
		
	}

}
