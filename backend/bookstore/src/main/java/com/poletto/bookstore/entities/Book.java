package com.poletto.bookstore.entities;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.poletto.bookstore.entities.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_book")
public class Book implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private LocalDate releaseDate;
	private String imgUrl;
	
	@Column (columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT (CURRENT_TIMESTAMP::timestamp(0))")
	private Instant registerDate;

	@Column(columnDefinition = "VARCHAR(255) CHECK (upper(status) IN ('AVAILABLE', 'BOOKED'))")
	private String status;
	
	@ManyToOne
	@JoinColumn (name = "author_id")
	private Author author;
	
	
	@OneToMany (mappedBy = "book")
	private Set<BookReservation> reservations = new HashSet<>();
	
	@ManyToMany
	@JoinTable(name = "tb_book_category", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Set<Category> categories = new HashSet<>();
	
	public Book() {

	}

	public Book(Long id, String name, Status status, Author author, LocalDate releaseDate, String imgUrl) {
		this.id = id;
		this.name = name;
		this.status = status.name();
		this.author = author;
		this.releaseDate = releaseDate;
		this.imgUrl = imgUrl;
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

	public Status getStatus() {
		return Status.valueOf(status);
	}

	public void setStatus(Status status) {
		this.status = status.name();
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
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

	public Set<BookReservation> getReservations() {
		return reservations;
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
		return "Book [id=" + id + ", name=" + name + ", author=" + author + ", releaseDate=" + releaseDate + ", imgUrl="
				+ imgUrl + "]";
	}

}
