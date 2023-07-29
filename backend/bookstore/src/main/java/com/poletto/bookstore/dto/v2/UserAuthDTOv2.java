package com.poletto.bookstore.dto.v2;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@JsonPropertyOrder(value = {"id"})
public class UserAuthDTOv2 extends UserDTOv2 {

	private static final long serialVersionUID = 2L;

	@NotNull
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{6,8}$", message = "deve conter entre 6 e 8 caracteres, com ao menos 1 letra e 1 número")
	private String password;
	
	private String token;

	public UserAuthDTOv2() {
		super();
	}

	public UserAuthDTOv2(String password, String token) {
		super();
		this.password = password;
		this.token = token;
	}
	
	public UserAuthDTOv2(UserDTOv2 userDTO, String password, String token) {
		setId(userDTO.getId());
		setFirstname(userDTO.getFirstname());
		setLastname(userDTO.getLastname());
		setEmail(userDTO.getEmail());
		setPassword(password);
		setToken(token);
		userDTO.getRoles().forEach(role -> this.roles.add(role));
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public Set<RoleDTOv2> getRoles() {
		return roles;
	}

	@Override
	public String toString() {
		return "UserAuthDTO [password=" + password + ", token=" + token + "]";
	}

}
