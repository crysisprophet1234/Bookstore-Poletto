package com.poletto.bookstore.dto.v2;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;

import jakarta.validation.constraints.Pattern;

@JsonPropertyOrder(value = {"id"})
public class RoleDTOv2 implements Serializable {

	private static final long serialVersionUID = 2L;

	@Mapping("id")
	@JsonProperty("id")
	private Long key;
	
	@Pattern (regexp = "ROLE_CUSTOMER|ROLE_OPERATOR|ROLE_ADMIN")
	private String authority;

	public RoleDTOv2() {
	}

	public RoleDTOv2(Long id, String authority) {
		super();
		this.key = id;
		this.authority = authority;
	}

	public Long getId() {
		return key;
	}

	public void setId(Long id) {
		this.key = id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public int hashCode() {
		return Objects.hash(key);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleDTOv2 other = (RoleDTOv2) obj;
		return Objects.equals(key, other.key);
	}

	@Override
	public String toString() {
		return "{id=" + key + ", authority=" + authority + "}";
	}
	
}