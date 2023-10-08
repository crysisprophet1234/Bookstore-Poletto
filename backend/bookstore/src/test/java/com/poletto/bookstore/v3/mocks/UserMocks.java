package com.poletto.bookstore.v3.mocks;

import com.poletto.bookstore.dto.v2.UserDTOv2;
import com.poletto.bookstore.entities.User;

public class UserMocks {

	public static UserDTOv2 userMockDto(Long id) {
		
		return new UserDTOv2(
				id,
				"firstname" + id,
				"lastname" + id,
				"mail" + id + "@mail.com"
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

}
