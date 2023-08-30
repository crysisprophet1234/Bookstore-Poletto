package com.poletto.bookstore.dto.v2;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;
import com.poletto.bookstore.converter.custom.UserMapper;
import com.poletto.bookstore.entities.User;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

@JsonPropertyOrder(value = { "id" })
public class ReservationDTOv2 extends RepresentationModel<ReservationDTOv2> implements Serializable {

	private static final long serialVersionUID = 2L;

	@Mapping("id")
	@JsonProperty("id")
	private Long key;

	@PastOrPresent
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
	private Instant moment;

	@Future
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate devolution;

	@Min(value = 1)
	private Integer weeks;

	@Pattern(regexp = "IN_PROGRESS|FINISHED")
	private String status;

	@Valid
	@NotNull
	@JsonIgnoreProperties({ "roles" })
	private UserDTOv2 client;
	
	@Valid
	private List<BookDTOv2> books = new ArrayList<>();

	public ReservationDTOv2() {

	}

	public ReservationDTOv2(Long id, Instant moment, String status, LocalDate devolution, Integer weeks, User client) {
		this.key = id;
		this.moment = moment;
		this.weeks = weeks;
		this.status = status;
		this.client = UserMapper.convertEntityToDtoV2(client);
		this.devolution = LocalDate.ofInstant(moment, ZoneId.of("America/Sao_Paulo")).plusWeeks(weeks);
	}

	public Long getId() {
		return key;
	}

	public void setId(Long id) {
		this.key = id;
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

	public UserDTOv2 getClient() {
		return client;
	}

	public void setClient(UserDTOv2 client) {
		this.client = client;
	}
	
	public List<BookDTOv2> getBooks() {
		return books;
	}

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
		ReservationDTOv2 other = (ReservationDTOv2) obj;
		return Objects.equals(key, other.key);
	}

	@Override
	public String toString() {
		return "ReservationDTOv2 [id=" + key + ", moment=" + moment + ", devolution=" + devolution + ", weeks=" + weeks
				+ ", status=" + status + ", client=" + client + ", books=" + books + "]";
	}

}
