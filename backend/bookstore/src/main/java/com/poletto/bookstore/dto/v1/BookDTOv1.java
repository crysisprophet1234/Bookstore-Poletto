package com.poletto.bookstore.dto.v1;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poletto.bookstore.entities.Author;
import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.Category;
import com.poletto.bookstore.entities.enums.BookStatus;

public class BookDTOv1 implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private LocalDate releaseDate;
	private String imgUrl;
	private String status;
	
	@JsonIgnoreProperties(value = {"nacionality", "books"})
	private Author author;
	
	private Set<CategoryDTOv1> categories = new HashSet<>();
	
	public BookDTOv1() {
		
	}

	public BookDTOv1(Long id, String name, LocalDate releaseDate, String imgUrl, BookStatus status, Author author) {
		this.id = id;
		this.name = name;
		this.releaseDate = releaseDate;
		this.imgUrl = imgUrl;
		this.status = status.name();
		this.author = author;
	}
	
	public BookDTOv1(Book entity) {
		id = entity.getId();
		name = entity.getName();
		releaseDate = entity.getReleaseDate();
		imgUrl = entity.getImgUrl();
		status = entity.getStatus().name();
		author = entity.getAuthor();
		categories = entity.getCategories().stream().map(x -> new CategoryDTOv1(x)).collect(Collectors.toSet());
	}
	
	public BookDTOv1(Book entity, Set<Category> categories) {
		this(entity);
		categories.forEach(cat -> this.categories.add(new CategoryDTOv1(cat)));
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

	public void setStatus(BookStatus status) {
		this.status = status.name();
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Set<CategoryDTOv1> getCategories() {
		return categories;
	}

	@Override
	public String toString() {
		return "BookDTO [id=" + id + ", name=" + name + ", releaseDate=" + releaseDate + ", imgUrl=" + imgUrl
				+ ", status=" + status + ", author=" + author + ", categories=" + categories + "]";
	}

}