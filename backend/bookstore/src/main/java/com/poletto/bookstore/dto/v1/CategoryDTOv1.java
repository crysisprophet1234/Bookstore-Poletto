package com.poletto.bookstore.dto.v1;

import java.io.Serializable;
import java.util.Objects;

import com.poletto.bookstore.entities.Category;

public class CategoryDTOv1 implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;

	public CategoryDTOv1() {

	}

	public CategoryDTOv1(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public CategoryDTOv1(Category entity) {
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
		CategoryDTOv1 other = (CategoryDTOv1) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "CategoryDTO [id=" + id + ", name=" + name + "]";
	}	

}
