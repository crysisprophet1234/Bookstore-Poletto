package com.poletto.bookstore.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poletto.bookstore.entities.Reservation;
import com.poletto.bookstore.entities.User;

public class ReservationDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Instant moment;
	private LocalDate devolution;
	private Integer weeks;
	
	@JsonIgnoreProperties("roles")
	private UserDTO client;

	private Set<BookDTO> books = new HashSet<>();

	public ReservationDTO() {

	}

	public ReservationDTO(Long id, Instant moment, LocalDate devolution, Integer weeks, User client) {
		this.id = id;
		this.moment = moment;
		this.devolution = devolution;
		this.weeks = weeks;
		this.client = new UserDTO(client);
	}

	public ReservationDTO(Reservation entity) {
		id = entity.getId();
		moment = entity.getMoment();
		devolution = entity.getDevolution();
		weeks = entity.getWeeks();
		client = createUser(entity.getClient());
		books = entity.getBooks().stream().map(x -> new BookDTO(x.getBook())).collect(Collectors.toSet());
		
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

	public UserDTO getClient() {
		return client;
	}

	public void setClient(UserDTO client) {
		this.client = client;
	}

	public Set<BookDTO> getBooks() {
		return books;
	}
	
	public UserDTO createUser(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setFirstName(user.getFirstname());
		userDTO.setLastName(user.getLastname());
		userDTO.setEmail(user.getEmail());
		return userDTO;
	}

}
