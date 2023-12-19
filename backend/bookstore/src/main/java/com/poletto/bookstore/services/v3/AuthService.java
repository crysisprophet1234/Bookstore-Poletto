package com.poletto.bookstore.services.v3;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.dto.v3.UserDto;

@Service("AuthServiceV3")
public interface AuthService {
	
	@Transactional
	UserDto register(UserDto dto);
	
	@Transactional(readOnly = true)
	UserDto authenticate(UserDto dto);

}
