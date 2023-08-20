package com.poletto.bookstore.dto.v2;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.URL;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;
import com.poletto.bookstore.entities.Author;
import com.poletto.bookstore.entities.enums.BookStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder(value = {"id"})
public class BookDTOv2 extends RepresentationModel<BookDTOv2> implements Serializable {

	private static final long serialVersionUID = 2L;
	
	@Mapping("id")
	@JsonProperty("id")
	private Long key;

	@Pattern(regexp = "^[A-Za-z0-9' -]+$", message = "aceita apenas letras e números")
    @Size(min = 1, max = 50)
	private String name;
	
	@PastOrPresent
	@NotNull
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate releaseDate;
	
	@URL
	@NotEmpty
	private String imgUrl;
	
	@Pattern (regexp = "AVAILABLE|BOOKED")
	private String status;
	
	@Valid
	@NotNull
	@JsonIgnoreProperties(value = {"nacionality", "books"})
	private Author author;

	@Valid
	@JsonIgnoreProperties(value = {"links"})
	private Set<CategoryDTOv2> categories = new HashSet<>();
	
	public BookDTOv2() {
		
	}

	public BookDTOv2(Long id, String name, LocalDate releaseDate, String imgUrl, BookStatus status, Author author) {
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

	public Set<CategoryDTOv2> getCategories() {
		return categories;
	}

	@Override
	public String toString() {
		return "BookDTOv2 [key=" + key + ", name=" + name + ", releaseDate=" + releaseDate + ", imgUrl=" + imgUrl
				+ ", status=" + status + ", author=" + author + ", categories=" + categories + "]";
	}

}
