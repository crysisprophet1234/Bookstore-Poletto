package com.poletto.bookstore.entities;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.poletto.bookstore.entities.enums.ReservationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservation")
public class Reservation implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Instant moment;
	
	@Column(columnDefinition = "VARCHAR(255) CHECK (upper(status) IN ('IN_PROGRESS', 'FINISHED')) DEFAULT 'IN_PROGRESS'")
	private String status;
	
	private LocalDate devolution;
	private Integer weeks;

	@ManyToOne
	@JoinColumn(name = "client_id")
	private User client;
	
	@OneToMany(mappedBy = "id.reservation")
	private Set<BookReservation> books = new HashSet<>();

	public Reservation() {

	}

	public Reservation(Long id, ReservationStatus status, Instant moment, User client, Integer weeks) {
		super();
		this.id = id;
		this.client = client;
		this.status = status.name();
		this.moment = moment;
		this.weeks = weeks;
		this.devolution = devolutionCalc(weeks);
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

	public User getClient() {
		return client;
	}

	public void setClient(User client) {
		this.client = client;
	}

	public LocalDate getDevolution() {
		return devolution;
	}

	public void setDevolution(LocalDate devolution) {
		this.devolution = devolution;
	}
	
	public ReservationStatus getStatus() {
		return ReservationStatus.valueOf(status);
	}

	public void setStatus(ReservationStatus status) {
		this.status = status.name();
	}

	public Integer getWeeks() {
		return weeks;
	}

	public void setWeeks(Integer weeks) {
		this.weeks = weeks;
	}

	public Set<BookReservation> getBooks() {
		return books;
	}
	
	public LocalDate devolutionCalc(Integer weeks) {
		return LocalDate.ofInstant(Instant.now(), ZoneId.of("America/Sao_Paulo")).plusWeeks(weeks);
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
		Reservation other = (Reservation) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", moment=" + moment + ", status=" + status + ", devolution=" + devolution
				+ ", weeks=" + weeks + ", client=" + client + ", books=" + books + "]";
	}

}
