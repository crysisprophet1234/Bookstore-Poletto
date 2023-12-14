package com.poletto.bookstore.dto.v2;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder(value = {"id"})
public class UserDTOv2 extends RepresentationModel<UserDTOv2> implements Serializable {

	private static final long serialVersionUID = 2L;

	@Mapping("id")
	@JsonProperty("id")
	private Long key;
	
	@Pattern(regexp = "^[A-Za-z]+$", message = "aceita apenas letras")
	@Size(min = 1, max = 30)
	@NotEmpty
	private String firstname;
	
	@Pattern(regexp = "^[A-Za-z]+(\\s[A-Za-z]+)*$", message = "aceita apenas letras")
	@Size(min = 1, max = 50)
	@NotEmpty
	private String lastname;
	
	@Email
	@NotEmpty
	private String email;

	@Valid
	Set<RoleDTOv2> roles = new HashSet<>();

	public UserDTOv2() {
		
	}

	public UserDTOv2(Long id, String firstname, String lastname, String email) {
		this.key = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
	}

	public Long getId() {
		return key;
	}

	public void setId(Long id) {
		this.key = id;
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

	public Set<RoleDTOv2> getRoles() {
		return roles;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, key);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDTOv2 other = (UserDTOv2) obj;
		return Objects.equals(email, other.email) && Objects.equals(key, other.key);
	}

	@Override
	public String toString() {
		return "UserDTOv2 [key=" + key + ", firstname=" + firstname + ", lastname=" + lastname + ", email=" + email
				+ ", roles=" + roles + "]";
	}

}
