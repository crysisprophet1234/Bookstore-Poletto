package com.poletto.bookstore.services.v3.impl;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poletto.bookstore.config.JwtService;
import com.poletto.bookstore.controllers.v3.UserController;
import com.poletto.bookstore.converter.v3.UserMapperV3;
import com.poletto.bookstore.dto.v3.RoleDto;
import com.poletto.bookstore.dto.v3.UserChangesDto;
import com.poletto.bookstore.dto.v3.UserDto;
import com.poletto.bookstore.entities.Role;
import com.poletto.bookstore.entities.User;
import com.poletto.bookstore.entities.VerificationToken;
import com.poletto.bookstore.entities.enums.AccountStatus;
import com.poletto.bookstore.entities.enums.UserStatus;
import com.poletto.bookstore.exceptions.AlreadyExistingAccountException;
import com.poletto.bookstore.exceptions.InvalidStatusException;
import com.poletto.bookstore.exceptions.InvalidTokenException;
import com.poletto.bookstore.exceptions.ObjectNotValidException;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.exceptions.UnauthorizedException;
import com.poletto.bookstore.repositories.v3.RoleRepository;
import com.poletto.bookstore.repositories.v3.UserRepository;
import com.poletto.bookstore.repositories.v3.VerificationTokenRepository;
import com.poletto.bookstore.services.v3.EmailService;
import com.poletto.bookstore.services.v3.UserService;

@Service("UserServiceV3")
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private EmailService emailService;

	@Override
	@Transactional(readOnly = true)
	public Page<UserDto> findAllPaged(Pageable pageable, String userStatus, String accountStatus, Long roleId) {

		Page<User> userEntityPage = userRepository.findAll(
			userStatus,
			accountStatus,
			roleId,
			pageable
		);
		
		logger.info("Resource USER page found: {}", userEntityPage);

		Page<UserDto> userDtoPage = userEntityPage.map(x -> UserMapperV3.INSTANCE.userToUserDto(x));

		userDtoPage.stream().forEach(dto -> userDtoWithLinks(dto));

		return userDtoPage;

	}

	@Override
	@Transactional(readOnly = true)
	public UserDto findById(Long userId) {

		User entity = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource USER not found. ID " + userId));

		logger.info("Resource USER found: " + entity.toString());

		return userDtoWithLinks(UserMapperV3.INSTANCE.userToUserDto(entity));

	}

	@Override
	@Transactional
	public UserDto updateEmail(Long userId, UserChangesDto userChangesDto) {

		String newEmail = userChangesDto.getEmail();

		if (newEmail == null || newEmail.isEmpty()) {
			throw new ObjectNotValidException("Novo e-mail não pode ser nulo");
		}

		User userEntity = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource USER not found, ID: " + userId));
		
		String tokenEmail = extractEmailFromJwt(userChangesDto.getToken());
		
		if (!userEntity.getEmail().equals(tokenEmail)) {
			throw new UnauthorizedException("Token inválido para atualização de e-mail");
		}

		if (userRepository.findByEmail(userChangesDto.getEmail()).isPresent()) {
			throw new AlreadyExistingAccountException(userChangesDto.getEmail());
		}

		userEntity.setEmail(userChangesDto.getEmail());

		userEntity.setAccountStatus(AccountStatus.UNVERIFIED);
		
		logger.info("Resource USER updated email: {user}", userEntity);

		return userDtoWithLinks(UserMapperV3.INSTANCE.userToUserDto(userEntity));

	}

	@Override
	@Transactional
	public UserDto updatePassword(Long userId, UserChangesDto userChangesDto) {

		String newPassword = userChangesDto.getPassword();

		if (newPassword == null || newPassword.isEmpty()) {
			throw new ObjectNotValidException("Nova senha não pode ser nula");
		}

		User userEntity = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource USER not found, ID: " + userId));
		
		String tokenEmail = extractEmailFromJwt(userChangesDto.getToken());
		
		if (!userEntity.getEmail().equals(tokenEmail)) {
			throw new UnauthorizedException("Token inválido para atualização de e-mail");
		}

		newPassword = passwordEncoder.encode(newPassword);

		userEntity.setPassword(newPassword);
		
		logger.info("Resource USER updated password: {user}", userEntity);

		return userDtoWithLinks(UserMapperV3.INSTANCE.userToUserDto(userEntity));

	}
	
	@Override
	@Transactional
	public UserDto addUserRoles(Long userId, Set<RoleDto> roles) {
		
		User userEntity = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource USER not found, ID: " + userId));
		
		for (RoleDto roleDto : roles) {
				
			Role role = roleRepository.findById(roleDto.getId()).orElseThrow(
					() -> new ResourceNotFoundException("Resource ROLE not found, ID: " + roleDto.getId()));

			userEntity.getRoles().add(role);
			
		}
		
		logger.info("Resource USER added roles: {user}", userEntity);
		
		return userDtoWithLinks(UserMapperV3.INSTANCE.userToUserDto(userEntity));
		
	}
	
	@Override
	@Transactional
	public UserDto removeUserRoles(Long userId, Set<RoleDto> roles) {
		
		User userEntity = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource USER not found, ID: " + userId));
		
		Set<Role> entityRolesToBeRemoved = new HashSet<>();
		
		for (RoleDto roleDto : roles) {
				
			Role role = roleRepository.findById(roleDto.getId()).orElseThrow(
					() -> new ResourceNotFoundException("Resource ROLE not found, ID: " + roleDto.getId()));
			
			if (userEntity.getRoles().contains(role)) {
				entityRolesToBeRemoved.add(role);
			}
			else throw new ObjectNotValidException("Usuário ID " + userId + " não possui a " + roleDto.toString() + ", impossível remover");
					
		}

		userEntity.getRoles().removeAll(entityRolesToBeRemoved);
		
		logger.info("Resource USER removed roles: {user}", userEntity);
		
		return userDtoWithLinks(UserMapperV3.INSTANCE.userToUserDto(userEntity));
		
	}

	@Override
	@Transactional
	public UserDto updateUserStatus(Long userId, UserChangesDto userChangesDto) {
		
		UserStatus newUserStatus = userChangesDto.getUserStatus();
		
		if (newUserStatus == null) {
			throw new IllegalArgumentException("Valor de UserStatus não pode ser nulo");
		}
			
		User userEntity = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource USER not found, ID: " + userId));
		
		if (userEntity.getUserStatus().equals(newUserStatus)) {
			throw new InvalidStatusException(userEntity);
		}
		
		userEntity.setUserStatus(newUserStatus);
		
		logger.info("Resource USER updated userStatus: {user}", userEntity);
		
		return userDtoWithLinks(UserMapperV3.INSTANCE.userToUserDto(userEntity));
		
	}
	
	@Override
	@Transactional
	public void sendAccountVerificationEmail(Long userId) {
		
		User userEntity = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource USER not found, ID: " + userId));
		
		VerificationToken verificationToken = verificationTokenRepository.findLastValidTokenByUserId(userEntity.getId())
				.orElseGet(() -> createVerificationToken(userEntity));
		
		//TODO: should be a link to the frontend page for verification
		String verifyAccountURL = ServletUriComponentsBuilder.fromCurrentContextPath()
		        .replacePath("api/v3/users/verify-account?token=" + verificationToken.getToken())
		        .build()
		        .toUriString();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		
		Map<String, Object> model = new HashMap<>();
		//TODO: person name when available
		model.put("username", "Mock username");
		model.put("verification_link", verifyAccountURL);
		model.put("expiration_date", formatter.format(verificationToken.getExpiresAt()));
		
		emailService.sendEmailFromTemplate(
			userEntity.getEmail(),
			"Verificação de conta Poletto Bookstore",
			"account-verification-template",
			model
		);
		
		logger.info("Account verification e-mail sent to {}, USER with ID {}", userEntity.getEmail(), userEntity.getId());
		
	}
	
	@Override
	@Transactional
	public void verifyAccount(UUID verificationToken) {
		
		VerificationToken verificationTokenEntity = verificationTokenRepository.findByToken(verificationToken)
				.orElseThrow(() -> new InvalidTokenException());
		
		if (verificationTokenEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
			throw new InvalidTokenException(verificationTokenEntity.getExpiresAt());
		}
		
		User userEntity = userRepository.getReferenceById(verificationTokenEntity.getUser().getId());
		
		userEntity.setAccountStatus(AccountStatus.VERIFIED);
		
		logger.info("Resource USER with id {} updated account status to VERIFIED", userEntity.getId());

	}
	
	private VerificationToken createVerificationToken(User user) {
		
		VerificationToken verificationToken = new VerificationToken();
		
		LocalDateTime now = LocalDateTime.now();
		verificationToken.setCreatedAt(now);
		verificationToken.setExpiresAt(now.plusDays(1));
		verificationToken.setUser(user);
		UUID token = UUID.randomUUID();
		verificationToken.setToken(token);
		
		verificationToken = verificationTokenRepository.save(verificationToken);
		
		logger.info("Resource VERIFICATION TOKEN created for USER with ID {}: [{}]", user.getId(), verificationToken);
		
		return verificationToken;
		
	}

	private String extractEmailFromJwt(String authHeader) {
		
		String email = null;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			email = jwtService.extractUsername(authHeader.substring(7));
		}

		return email;

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
