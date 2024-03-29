package com.poletto.bookstore.dto.v1;

import java.io.Serializable;
import java.util.Objects;

import com.poletto.bookstore.entities.Role;

public class RoleDTOv1 implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String authority;

	public RoleDTOv1() {
	}

	public RoleDTOv1(Long id, String authority) {
		super();
		this.id = id;
		this.authority = authority;
	}

	public RoleDTOv1(Role entity) {
		super();
		id = entity.getId();
		authority = entity.getAuthority();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleDTOv1 other = (RoleDTOv1) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "RoleDTO [id=" + id + ", authority=" + authority + "]";
	}
	
}