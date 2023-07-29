package com.poletto.bookstore.dto.v1;

import java.io.Serializable;
import java.util.Objects;

import com.poletto.bookstore.entities.Author;

public class AuthorDTOv1 implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	
	public AuthorDTOv1() {
		
	}
	
	public AuthorDTOv1(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public AuthorDTOv1(Author entity) {
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
		AuthorDTOv1 other = (AuthorDTOv1) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "AuthorDTO [id=" + id + ", name=" + name + "]";
	}
	
}
