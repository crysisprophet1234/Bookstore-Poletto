package com.poletto.bookstore.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import com.poletto.bookstore.entities.enums.BookStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "book")
public class Book implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	
	@Column(columnDefinition = "VARCHAR(1500)")
	private String description;
	
	private String language;
	private Integer numberOfPages;
	private LocalDate publicationDate;
	private String imgUrl;
	
	@Column(columnDefinition = "VARCHAR(255) DEFAULT 'ACTIVE' CHECK (upper(status) IN ('ACTIVE', 'INACTIVE'))")
	@Enumerated(EnumType.STRING)
	private BookStatus status;
	
	@ManyToOne
	@JoinColumn (name = "author_id")
	private Author author;
	
	@OneToOne(mappedBy = "book", cascade = CascadeType.ALL)
    private BookStock stock;
	
	@OneToMany (mappedBy = "id.book", fetch = FetchType.EAGER)
	private Set<BookReservation> reservations = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "book_category", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Set<Category> categories = new TreeSet<>(Comparator.comparing(Category::getId));
	
	public Book() {

	}

	public Book(
			Long id,
			String title,
			String description,
			String language,
			Integer numberOfPages,
			BookStatus status,
			BookStock bookStock,
			Author author,
			LocalDate publicationDate,
			String imgUrl
			) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.language = language;
		this.numberOfPages = numberOfPages;
		this.status = status;
		this.stock = bookStock;
		this.author = author;
		this.publicationDate = publicationDate;
		this.imgUrl = imgUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	
	public BookStatus getStatus() {
		return status;
	}

	public BookStock getStock() {
		return stock;
	}

	public void setStock(BookStock stock) {
		this.stock = stock;
	}

	public void setStatus(BookStatus status) {
		this.status = status;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
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

	public Set<Reservation> getReservations() {
		Set<Reservation> set = new HashSet<>();
		for (BookReservation x : reservations) {
			set.add(x.getReservation());
		}
		return set;
	}

	public Set<Category> getCategories() {
		return categories;
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
		Book other = (Book) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", description=" + description + ", language=" + language
				+ ", numberOfPages=" + numberOfPages + ", publicationDate=" + publicationDate + ", imgUrl=" + imgUrl
				+ ", status=" + status + ", author=" + author + ", stock=" + stock + ", categories=" + categories + "]";
	}

}
