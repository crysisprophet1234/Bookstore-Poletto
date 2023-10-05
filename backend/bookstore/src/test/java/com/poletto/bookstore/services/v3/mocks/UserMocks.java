package com.poletto.bookstore.services.v3.mocks;

import com.poletto.bookstore.dto.v2.UserDTOv2;

public class UserMocks {

	public static UserDTOv2 userMock(Long id) {
		
		return new UserDTOv2(
				id,
				"firstname" + id,
				"lastname" + id,
				"mail" + id + "@mail.com"
		);
		
	}
	
	public static UserDTOv2 userMock() {
		
		return new UserDTOv2(
				1L,
				"firstname",
				"lastname",
				"mail@mail.com"
		);
		
	}

}
