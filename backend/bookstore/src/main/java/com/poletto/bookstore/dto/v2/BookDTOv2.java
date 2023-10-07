package com.poletto.bookstore.dto.v2;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.validator.constraints.URL;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;
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

	@Pattern(regexp = "^[A-Za-z0-9'& -#]+$", message = "aceita apenas letras e números")
    @Size(min = 1, max = 50)
	private String name;
	
	@PastOrPresent
	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate releaseDate;
	
	@URL
	@NotEmpty
	private String imgUrl;
	
	private BookStatus status;
	
	@Valid
	@NotNull
	private AuthorDTOv2 author;

	@Valid
	@JsonIgnoreProperties(value = {"links"})
	private Set<CategoryDTOv2> categories = new HashSet<>();
	
	public BookDTOv2() {
		
	}

	public BookDTOv2(Long id, String name, LocalDate releaseDate, String imgUrl, BookStatus status, AuthorDTOv2 author) {
		this.key = id;
		this.name = name;
		this.releaseDate = releaseDate;
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

	public BookStatus getStatus() {
		return status;
	}

	public void setStatus(BookStatus status) {
		this.status = status;
	}

	public AuthorDTOv2 getAuthor() {
		return author;
	}

	public void setAuthor(AuthorDTOv2 author) {
		this.author = author;
	}

	public Set<CategoryDTOv2> getCategories() {
		return categories;
	}
	
	//TODO could have potential
//	@Override
//	@JsonProperty("links")
//	@JsonInclude(Include.NON_EMPTY)
//	@JsonSerialize(using = Jackson2HalModule.HalLinkListSerializer.class)
//	@JsonDeserialize(using = Jackson2HalModule.HalLinkListDeserializer.class)
//	public Links getLinks() {
//	  return super.getLinks();
//	}
	
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
		BookDTOv2 other = (BookDTOv2) obj;
		return Objects.equals(key, other.key);
	}

	@Override
	public String toString() {
		return "BookDTOv2 [key=" + key + ", name=" + name + ", releaseDate=" + releaseDate + ", imgUrl=" + imgUrl
				+ ", status=" + status + ", author=" + author + ", categories=" + categories + "]";
	}

}
