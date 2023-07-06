package com.poletto.bookstore.dto.v2;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder(value = {"id"})
public class CategoryDTOv2 extends RepresentationModel<CategoryDTOv2> implements Serializable {

	private static final long serialVersionUID = 2L;

	@Mapping("id")
	@JsonProperty("id")
	private Long key;
	
	@Pattern(regexp = "^[A-Za-z]+$", message = "aceita apenas letras")
	@Size(min = 1, max = 50)
    private String name;

	public CategoryDTOv2() {

	}

	public CategoryDTOv2(Long id, String name) {
		this.key = id;
		this.name = name;
	}

	public Long getId() {
		return key;
	}

	public void setId(Long id) {
		this.key = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		CategoryDTOv2 other = (CategoryDTOv2) obj;
		return Objects.equals(key, other.key);
	}

	@Override
	public String toString() {
		return "CategoryDTOv2 [key=" + key + ", name=" + name + "]";
	}	

}