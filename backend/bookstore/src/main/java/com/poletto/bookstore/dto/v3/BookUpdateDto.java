package com.poletto.bookstore.dto.v3;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.URL;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class BookUpdateDto implements Serializable {
	
	private static final long serialVersionUID = 3L;
	
	@Pattern(regexp = "^[A-Za-z0-9'& -#]+$", message = "aceita apenas letras e números")
    @Size(min = 1, max = 50)
	@NotBlank
	private String title;

    @Size(min = 10, max = 1000)
    @NotBlank
	private String description;

	@Pattern(regexp = "^[A-Za-z]+$", message = "aceita apenas letras")
    @Size(min = 1, max = 50)
	@NotBlank
	private String language;
	
	@Min(5)
	@Max(5000)
	@NotNull
	private Integer numberOfPages;
	
	@PastOrPresent
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Sao_Paulo")
	@NotNull
	private LocalDate publicationDate;
	
	@URL
	@NotBlank
	private String imgUrl;
	
	@NotNull
	private AuthorDto author;
	
	@NotEmpty
	private List<CategoryDto> categories = new ArrayList<>();
	
	public BookUpdateDto() {
		
	}

	public BookUpdateDto(
			String title,
			String description,
			String language,
			Integer numberOfPages,
			LocalDate publicationDate,
			String imgUrl,
			AuthorDto author,
			List<CategoryDto> categories) {
		this.title = title;
		this.description = description;
		this.language = language;
		this.numberOfPages = numberOfPages;
		this.publicationDate = publicationDate;
		this.imgUrl = imgUrl;
		this.author = author;
		this.categories = categories;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(Integer numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	public LocalDate getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(LocalDate publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public AuthorDto getAuthor() {
		return author;
	}

	public void setAuthor(AuthorDto author) {
		this.author = author;
	}

	public List<CategoryDto> getCategories() {
		return categories;
	}

	@Override
	public String toString() {
		return "BookUpdateDto [title=" + title + ", description=" + description + ", language=" + language
				+ ", numberOfPages=" + numberOfPages + ", publicationDate=" + publicationDate + ", imgUrl=" + imgUrl
				+ ", author=" + author + ", categories=" + categories + "]";
	}

}
