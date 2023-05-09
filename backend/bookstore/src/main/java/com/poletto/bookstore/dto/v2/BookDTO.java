package com.poletto.bookstore.dto.v2;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dozermapper.core.Mapping;
import com.poletto.bookstore.dto.v1.CategoryDTO;
import com.poletto.bookstore.entities.Author;
import com.poletto.bookstore.entities.enums.BookStatus;

public class BookDTO extends RepresentationModel<BookDTO> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Mapping("id")
	@JsonProperty("id")
	private Long key;

	private String name;
	private LocalDate releaseDate;
	private String imgUrl;
	private String status;
	private Author author;

	private Set<CategoryDTO> categories = new HashSet<>();
	
	public BookDTO() {
		
	}

	public BookDTO(Long id, String name, LocalDate releaseDate, String imgUrl, BookStatus status, Author author) {
		this.key = id;
		this.name = name;
		this.releaseDate = releaseDate;
		this.imgUrl = imgUrl;
		this.status = status.name();
		this.author = author;
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

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(BookStatus status) {
		this.status = status.name();
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Set<CategoryDTO> getCategories() {
		return categories;
	}

	@Override
	public String toString() {
		return "BookDTO [key=" + key + ", name=" + name + ", releaseDate=" + releaseDate + ", imgUrl=" + imgUrl
				+ ", status=" + status + ", author=" + author + ", categories=" + categories + ", getLinks()="
				+ getLinks() + "]";
	}

}
