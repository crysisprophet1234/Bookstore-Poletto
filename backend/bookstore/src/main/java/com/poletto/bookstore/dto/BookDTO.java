package com.poletto.bookstore.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poletto.bookstore.entities.Author;
import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.Category;

public class BookDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private LocalDate releaseDate;
	private String imgUrl;
	private String status;
	
	@JsonIgnoreProperties("nacionality")
	private Author author;
	
	private Set<CategoryDTO> categories = new HashSet<>();
	
	public BookDTO() {
		
	}

	public BookDTO(Long id, String name, LocalDate releaseDate, String imgUrl, String status, Author author) {
		this.id = id;
		this.name = name;
		this.releaseDate = releaseDate;
		this.imgUrl = imgUrl;
		this.status = status;
		this.author = author;
	}
	
	public BookDTO(Book entity) {
		id = entity.getId();
		name = entity.getName();
		releaseDate = entity.getReleaseDate();
		imgUrl = entity.getImgUrl();
		status = entity.getStatus().name();
		author = entity.getAuthor();
		categories = entity.getCategories().stream().map(x -> new CategoryDTO(x)).collect(Collectors.toSet());
	}
	
	public BookDTO(Book entity, Set<Category> categories) {
		this(entity);
		categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
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

	public void setStatus(String status) {
		this.status = status;
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
		return "BookDTO [id=" + id + ", name=" + name + ", releaseDate=" + releaseDate + ", imgUrl=" + imgUrl
				+ ", status=" + status + ", author=" + author + ", categories=" + categories + "]";
	}

}
