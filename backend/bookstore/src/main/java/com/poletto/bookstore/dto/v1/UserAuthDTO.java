package com.poletto.bookstore.dto.v1;

import java.util.Set;

public class UserAuthDTO extends UserDTO {

	private static final long serialVersionUID = 1L;

	private String password;
	private String token;

	public UserAuthDTO() {
		super();
	}

	public UserAuthDTO(String password, String token) {
		super();
		this.password = password;
		this.token = token;
	}
	
	public UserAuthDTO(UserDTO userDTO, String password, String token) {
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
	
	public Set<RoleDTO> getRoles() {
		return roles;
	}

	@Override
	public String toString() {
		return "UserAuthDTO [password=" + password + ", token=" + token + "]";
	}

}
