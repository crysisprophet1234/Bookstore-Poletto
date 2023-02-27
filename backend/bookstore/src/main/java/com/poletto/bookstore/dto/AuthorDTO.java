package com.poletto.bookstore.dto;

import java.io.Serializable;
import java.util.Objects;

import com.poletto.bookstore.entities.Author;

public class AuthorDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	
	public AuthorDTO() {
		
	}
	
	public AuthorDTO(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public AuthorDTO(Author entity) {
		super();
		id = entity.getId();
		name = entity.getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		AuthorDTO other = (AuthorDTO) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "AuthorDTO [id=" + id + ", name=" + name + "]";
	}
	
}
