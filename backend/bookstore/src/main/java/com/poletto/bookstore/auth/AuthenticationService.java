package com.poletto.bookstore.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.poletto.bookstore.config.JwtService;
import com.poletto.bookstore.dto.RoleDTO;
import com.poletto.bookstore.dto.UserDTO;
import com.poletto.bookstore.dto.UserInsertDTO;
import com.poletto.bookstore.dto.UserLoginDTO;
import com.poletto.bookstore.entities.Role;
import com.poletto.bookstore.entities.User;
import com.poletto.bookstore.repositories.RoleRepository;
import com.poletto.bookstore.repositories.UserRepository;

@Service
public class AuthenticationService {

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

	public UserDTO register(UserInsertDTO dto) {

		User entity = new User();
		
		copyDtoToEntity(dto, entity);
		
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		
		entity = userRepository.save(entity);

		return new UserDTO(entity);

	}

	public UserLoginDTO authenticate(UserLoginDTO dto) {  //TODO verificar dados presentes na response

		authenticationManager.authenticate(
								new UsernamePasswordAuthenticationToken(
										dto.getEmail(),
										dto.getPassword()
										)
								);

		User entity = userRepository.findByEmail(dto.getEmail()).orElseThrow();

		String jwtToken = jwtService.generateToken(entity);

		return new UserLoginDTO(entity.getId(), entity.getEmail(), jwtToken);

	}

	private void copyDtoToEntity(UserDTO dto, User entity) {

		entity.setFirstname(dto.getFirstname());
		entity.setLastname(dto.getLastname());
		entity.setEmail(dto.getEmail());

		entity.getRoles().clear();
		for (RoleDTO roleDto : dto.getRoles()) {
			Role role = roleRepository.getReferenceById(roleDto.getId());
			entity.getRoles().add(role);
		}

	}

}
