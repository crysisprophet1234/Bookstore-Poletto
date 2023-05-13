package com.poletto.bookstore.dto.v1;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.poletto.bookstore.entities.Role;
import com.poletto.bookstore.entities.User;

public class UserDTOv1 implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String firstname;
	private String lastname;
	private String email;

	Set<RoleDTOv1> roles = new HashSet<>();

	public UserDTOv1() {
	}

	public UserDTOv1(Long id, String firstname, String lastname, String email) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
	}

	public UserDTOv1(User entity) {
		id = entity.getId();
		firstname = entity.getFirstname();
		lastname = entity.getLastname();
		email = entity.getEmail();
		for (Role role : entity.getRoles()) {
			this.roles.add(new RoleDTOv1(role));
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Set<RoleDTOv1> getRoles() {
		return roles;
	}

}
