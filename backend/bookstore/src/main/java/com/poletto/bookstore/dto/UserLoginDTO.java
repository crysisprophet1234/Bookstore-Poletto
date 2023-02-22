package com.poletto.bookstore.dto;

public class UserLoginDTO extends UserInsertDTO {

	private static final long serialVersionUID = 1L;

	private String token;

	public UserLoginDTO() {
		super();
	}

	public UserLoginDTO(Long id, String email, String token) {
		super();
		setId(id);
		setEmail(email);
		setToken(token);
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
