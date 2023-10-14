package com.poletto.bookstore.v3.mocks;

import com.poletto.bookstore.dto.v2.UserAuthDTOv2;
import com.poletto.bookstore.dto.v2.UserDTOv2;
import com.poletto.bookstore.entities.User;

public class UserMocks {

	public static UserDTOv2 userMockDto(Long id) {
		
		return new UserDTOv2(
				id,
				"firstname" + id,
				"lastname" + id,
				"user" + id + "@mail.com"
		);
		
	}
	
	public static UserDTOv2 userMockDto() {
		
		return new UserDTOv2(
				1L,
				"john",
				"doe",
				"johndoe@mail.com"
		);
		
	}
	
	public static UserAuthDTOv2 userAuthDto(Long id) {
		
		return new UserAuthDTOv2(
				userMockDto(id),
				"psw",
				"token"
		);
		
	}
	
	public static UserAuthDTOv2 userAuthDto() {
		
		return new UserAuthDTOv2(
				userMockDto(),
				"psw",
				"token"
		);
		
	}
	
	public static User userMockEntity(Long id) {
		
		return new User(
				id,
				"mail" + id + "@mail.com",
				"psw" + id,
				"firstname" + id,
				"lastname" + id
		);
		
	}
	
	public static User userMockEntity() {
		
		return new User(
				1L,
				"johndoe@mail.com",
				"psw",
				"john",
				"doe"
		);
		
	}

	public static UserAuthDTOv2 registerUserMockDto() {
		
		UserAuthDTOv2 userAuthDto = new UserAuthDTOv2();
		userAuthDto.setFirstname("john");
		userAuthDto.setLastname("doe");
		userAuthDto.setEmail("johndoe@mail.com");
		userAuthDto.setPassword("psw");
		
		return userAuthDto;
		
	}
	
	public static UserAuthDTOv2 loginUserMockDto() {
		
		UserAuthDTOv2 userAuthDto = new UserAuthDTOv2();
		userAuthDto.setEmail("johndoe@mail.com");
		userAuthDto.setPassword("psw");
		
		return userAuthDto;
		
	}
	
	public static UserAuthDTOv2 loginUserMockDto(UserAuthDTOv2 dto) {
		
		UserAuthDTOv2 userAuthDto = new UserAuthDTOv2();
		userAuthDto.setEmail(dto.getEmail());
		userAuthDto.setPassword(dto.getPassword());
		
		return userAuthDto;
		
	}
	
}
