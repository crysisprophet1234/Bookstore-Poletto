package com.poletto.bookstore.auth;

import java.util.HashSet;
import java.util.Set;

import com.poletto.bookstore.dto.RoleDTO;

public class RegisterRequest {
	
	private String firstname;
	private String lastname;
	private String email;
	private String password;
	
	Set<RoleDTO> roles = new HashSet<>();
	
	public RegisterRequest () {
		
	}
	
	public RegisterRequest(String firstname, String lastname, String email, String password) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
