package com.poletto.bookstore.v3.mocks;

import com.poletto.bookstore.dto.v3.UserDto;
import com.poletto.bookstore.entities.User;
import com.poletto.bookstore.entities.enums.AccountStatus;
import com.poletto.bookstore.entities.enums.UserStatus;

public class UserMocks {

	public static UserDto userMockDto(Long id) {
		
		UserDto userDto = new UserDto();
		userDto.setKey(id);
		userDto.setEmail("mock@mail.com");
		userDto.setPassword("mockpsw123");
		userDto.setUserStatus(UserStatus.ACTIVE);
		userDto.setAccountStatus(AccountStatus.VERIFIED);
		
		return userDto;
		
	}
	
	public static UserDto userMockDto() {
		
		UserDto userDto = new UserDto();
		userDto.setKey(1L);
		userDto.setEmail("mock@mail.com");
		userDto.setPassword("mockpsw123");
		userDto.setUserStatus(UserStatus.ACTIVE);
		userDto.setAccountStatus(AccountStatus.VERIFIED);
		
		return userDto;
		
	}
	
	public static User userMockEntity(Long id) {
		
		return new User(
			id,
			"mock@mail.com",
			"mockpsw123",
			AccountStatus.VERIFIED,
			UserStatus.ACTIVE
		);
		
	}
	
	public static User userMockEntity() {
		
		return new User(
			1L,
			"mock@mail.com",
			"mockpsw123",
			AccountStatus.VERIFIED,
			UserStatus.ACTIVE
		);
		
	}

	public static UserDto registerUserMockDto() {
		
		UserDto userDto = new UserDto();
		userDto.setEmail("mock@mail.com");
		userDto.setPassword("mockpsw123");
		
		return userDto;
		
	}
	
}
