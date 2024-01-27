package com.poletto.bookstore.services.v3;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.dto.v3.RoleDto;
import com.poletto.bookstore.dto.v3.UserUpdateDto;
import com.poletto.bookstore.dto.v3.UserDto;

@Service("UserServiceV3")
public interface UserService {

	@Transactional(readOnly = true)
	Page<UserDto> findAllPaged(Pageable pageable, String userStatus, String accountStatus, Long roleId);
	
	@Transactional(readOnly = true)
	UserDto findById(Long userId);
	
	@Transactional
	UserDto updateEmail(Long userId, UserUpdateDto userChangesDto);
	
	@Transactional
	UserDto updatePassword(Long userId, UserUpdateDto userChangesDto);
	
	@Transactional
	UserDto addUserRoles(Long userId, Set<RoleDto> roles);
	
	@Transactional
	UserDto removeUserRoles(Long userId, Set<RoleDto> roles);
	
	@Transactional
	UserDto updateUserStatus(Long userId, UserUpdateDto userChangesDto);
	
	@Transactional(readOnly = true)
	void sendAccountVerificationEmail(Long userId);
	
	@Transactional
	void verifyAccount(UUID verificationToken);
	
}
