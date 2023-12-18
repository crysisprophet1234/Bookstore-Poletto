package com.poletto.bookstore.dto.v3;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.poletto.bookstore.entities.enums.AccountStatus;
import com.poletto.bookstore.entities.enums.UserStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@JsonPropertyOrder({"id"})
@JsonInclude(JsonInclude.Include.NON_NULL) //TODO: is this right or just leave null?
public class UserDto extends RepresentationModel<UserDto> implements Serializable {

	private static final long serialVersionUID = 3L;
	
	@JsonProperty("id")
	private Long key;
	
	@Email
	@NotEmpty
	private String email;
	
	@NotNull
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{6,12}$", message = "deve conter entre 6 e 12 caracteres, com ao menos 1 letra e 1 número")
	private String password;
	
	private String token;

	@Enumerated(EnumType.STRING)
	private AccountStatus accountStatus;
	
	@Enumerated(EnumType.STRING)
	private UserStatus userStatus;
	
	@Valid
	private Set<RoleDto> roles = new TreeSet<>(Comparator.comparing(RoleDto::getId));
	
	public UserDto() {

	}

	public UserDto(
			Long key,
			String email,
			String password,
			String token,
			AccountStatus accountStatus,
			UserStatus userStatus,
			Set<RoleDto> roles
		) {
		
		this.key = key;
		this.email = email;
		this.password = password;
		this.token = token;
		this.accountStatus = accountStatus;
		this.userStatus = userStatus;
		this.roles = roles;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
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

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public Set<RoleDto> getRoles() {
		return roles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(email, key);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDto other = (UserDto) obj;
		return Objects.equals(email, other.email) && Objects.equals(key, other.key);
	}

	@Override
	public String toString() {
		return "UserDto [key=" + key + ", email=" + email + ", password=" + password + ", token=" + token
				+ ", accountStatus=" + accountStatus + ", userStatus=" + userStatus + ", roles=" + roles + "]";
	}

}
