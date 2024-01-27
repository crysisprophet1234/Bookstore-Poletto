package com.poletto.bookstore.dto.v3;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.validator.constraints.URL;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.poletto.bookstore.entities.enums.BookStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder({"id"})
public class BookDto extends RepresentationModel<BookDto> implements Serializable {

	private static final long serialVersionUID = 3L;

	@JsonProperty("id")
	private Long key;
	
	@Pattern(regexp = "^[A-Za-z0-9'& -#]+$", message = "aceita apenas letras e números")
	@NotBlank
    @Size(min = 1, max = 50)
	private String title;

	@NotBlank
    @Size(min = 10, max = 1000)
	private String description;
	
	@Pattern(regexp = "^[A-Za-z]+$", message = "aceita apenas letras")
	@NotBlank
    @Size(min = 1, max = 50)
	private String language;
	
	@NotNull
	@Min(5)
	@Max(5000)
	private Integer numberOfPages;
	
	@PastOrPresent
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Sao_Paulo")
	@NotNull
	private LocalDate publicationDate;
	
	@URL
	@NotBlank
	private String imgUrl;
	
	private BookStatus status;
	
	@NotNull
	private AuthorDto author;
	
	@NotEmpty
	private Set<CategoryDto> categories = new TreeSet<>(Comparator.comparing(CategoryDto::getId));
	
	public BookDto() {
		
	}

	public BookDto(
			Long id,
			String title,
			String description,
			String language,
			Integer numberOfPages,
			LocalDate publicationDate,
			String imgUrl,
			BookStatus status,
			AuthorDto author
		) {
		this.key = id;
		this.title = title;
		this.description = description;
		this.language = language;
		this.numberOfPages = numberOfPages;
		this.publicationDate = publicationDate;
		this.imgUrl = imgUrl;
		this.status = status;
		this.author = author;
	}

	public Long getId() {
		return key;
	}

	public void setId(Long id) {
		this.key = id;
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

	public BookStatus getStatus() {
		return status;
	}

	public void setStatus(BookStatus status) {
		this.status = status;
	}

	public AuthorDto getAuthor() {
		return author;
	}

	public void setAuthor(AuthorDto author) {
		this.author = author;
	}

	public Set<CategoryDto> getCategories() {
		return categories;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(author, key);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookDto other = (BookDto) obj;
		return Objects.equals(author, other.author) && Objects.equals(key, other.key);
	}

	@Override
	public String toString() {
		return "BookDto [key=" + key + ", title=" + title + ", description=" + description.substring(0, 30) + "..." + ", language=" + language
				+ ", numberOfPages=" + numberOfPages + ", publicationDate=" + publicationDate + ", imgUrl=" + imgUrl
				+ ", status=" + status + ", author=" + author + ", categories=" + categories + "]";
	}

}
