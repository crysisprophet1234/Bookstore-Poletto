package com.poletto.bookstore.dto.v3;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.poletto.bookstore.entities.enums.AccountStatus;
import com.poletto.bookstore.entities.enums.UserStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public class UserChangesDto implements Serializable {

	private static final long serialVersionUID = 3L;
	
	@Email
	private String email;
	
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{6,12}$", message = "deve conter entre 6 e 12 caracteres, com ao menos 1 letra e 1 número")
	private String password;
	
	private AccountStatus accountStatus;
	
	private UserStatus userStatus;
	
	private String token;
	
	@Valid
	private Set<RoleDto> roles = new HashSet<>();
	
	public UserChangesDto() {
		
	}

	public UserChangesDto(
			String email,
			String password,
			AccountStatus accountStatus,
			UserStatus userStatus,
			String token
		) {
		this.email = email;
		this.password = password;
		this.accountStatus = accountStatus;
		this.userStatus = userStatus;
		this.token = token;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Set<RoleDto> getRoles() {
		return roles;
	}

	@Override
	public String toString() {
		return "UserChangesDto [email=" + email + ", password=" + password + ", accountStatus=" + accountStatus
				+ ", userStatus=" + userStatus + ", token=" + token + ", roles=" + roles + "]";
	}
	
}
