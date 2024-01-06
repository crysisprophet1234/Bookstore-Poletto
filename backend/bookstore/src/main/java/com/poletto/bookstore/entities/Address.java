package com.poletto.bookstore.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "address")
public class Address implements Serializable {

	private static final long serialVersionUID = 3L;
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition = "VARCHAR(9) CHECK (cep ~ '^[0-9]{8}$')")
	private String cep;
	
	private String addressName;
	private Integer number;
	private String complement;
	private String district;
	private String city;
	
	@Column(columnDefinition = "VARCHAR(2) CHECK (state ~ '^[A-Z]{2}$')")
	private String state;
	
	@ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
	
	public Address() {	
	}

	public Address(
			Long id,
			String cep,
			String addressName,
			Integer number,
			String complement,
			String district,
			String city,
			String state
		) {
		this.id = id;
		this.cep = cep;
		this.addressName = addressName;
		this.number = number;
		this.complement = complement;
		this.district = district;
		this.city = city;
		this.state = state;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public int hashCode() {
		return Objects.hash(addressName, cep, city, complement, district, id, number, state);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		return Objects.equals(addressName, other.addressName) && Objects.equals(cep, other.cep)
				&& Objects.equals(city, other.city) && Objects.equals(complement, other.complement)
				&& Objects.equals(district, other.district) && Objects.equals(id, other.id)
				&& Objects.equals(number, other.number) && Objects.equals(state, other.state);
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", cep=" + cep + ", addressName=" + addressName + ", number=" + number
				+ ", complement=" + complement + ", district=" + district + ", city=" + city + ", state=" + state + "]";
	}
	
}
