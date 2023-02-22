package com.poletto.bookstore.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.poletto.bookstore.config.JwtService;
import com.poletto.bookstore.dto.RoleDTO;
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

	public AuthenticationResponse register(RegisterRequest request) {

		User entity = new User();
		
		copyRequestToEntity(request, entity);
		
		entity.setPassword(passwordEncoder.encode(request.getPassword()));
		
		userRepository.save(entity);

		String jwtToken = jwtService.generateToken(entity);

		return new AuthenticationResponse(jwtToken);

	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {

		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

		var jwtToken = jwtService.generateToken(user);

		return new AuthenticationResponse(jwtToken);

	}

	private void copyRequestToEntity(RegisterRequest request, User entity) {

		entity.setFirstname(request.getFirstname());
		entity.setLastname(request.getLastname());
		entity.setEmail(request.getEmail());

		entity.getRoles().clear();
		for (RoleDTO roleDto : request.getRoles()) {
			Role role = roleRepository.getOne(roleDto.getId());
			entity.getRoles().add(role);
		}

	}

}
