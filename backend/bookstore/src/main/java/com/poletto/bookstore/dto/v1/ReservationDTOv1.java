package com.poletto.bookstore.dto.v1;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poletto.bookstore.entities.BookReservation;
import com.poletto.bookstore.entities.Reservation;
import com.poletto.bookstore.entities.User;

public class ReservationDTOv1 implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Instant moment;
	private LocalDate devolution;
	private Integer weeks;
	private String status;
	
	@JsonIgnoreProperties("roles")
	private UserDTOv1 client;

	
	@JsonIgnoreProperties({"imgUrl", "categories", "releaseDate"})
	private Set<BookDTOv1> books = new HashSet<>();

	public ReservationDTOv1() {

	}

	public ReservationDTOv1(Long id, Instant moment, String status, LocalDate devolution, Integer weeks, User client) {
		this.id = id;
		this.moment = moment;
		this.weeks = weeks;
		this.status = status;
		this.client = new UserDTOv1(client);
		this.devolution = LocalDate.ofInstant(moment, ZoneId.of("America/Sao_Paulo")).plusWeeks(weeks);
	}

	public ReservationDTOv1(Reservation entity) {
		id = entity.getId();
		moment = entity.getMoment();
		devolution = entity.getDevolution();
		weeks = entity.getWeeks();
		status = entity.getStatus().name();
		client = createUser(entity.getClient());
		for (BookReservation bookReservation : entity.getBooks()) {
			books.add(new BookDTOv1(bookReservation.getBook()));
		}
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instant getMoment() {
		return moment;
	}

	public void setMoment(Instant moment) {
		this.moment = moment;
	}

	public LocalDate getDevolution() {
		return devolution;
	}

	public void setDevolution(LocalDate devolution) {
		this.devolution = devolution;
	}

	public Integer getWeeks() {
		return weeks;
	}

	public void setWeeks(Integer weeks) {
		this.weeks = weeks;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UserDTOv1 getClient() {
		return client;
	}

	public void setClient(UserDTOv1 client) {
		this.client = client;
	}

	public Set<BookDTOv1> getBooks() {
		return books;
	}
	
	public UserDTOv1 createUser(User user) {
		UserDTOv1 userDTO = new UserDTOv1();
		userDTO.setId(user.getId());
		userDTO.setFirstname(user.getFirstname());
		userDTO.setLastname(user.getLastname());
		userDTO.setEmail(user.getEmail());
		return userDTO;
	}

	@Override
	public String toString() {
		return "ReservationDTO [id=" + id + ", moment=" + moment + ", devolution=" + devolution + ", weeks=" + weeks
				+ ", status=" + status + ", client=" + client + ", books=" + books + "]";
	}

}
