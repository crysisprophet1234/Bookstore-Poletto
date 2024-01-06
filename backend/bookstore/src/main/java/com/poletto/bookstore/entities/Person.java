package com.poletto.bookstore.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "person")
public class Person implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
	private String gender;
	private String phone;
	private String nationality;
	private String profilePictureUrl;
	
	@Column(columnDefinition = "VARCHAR(11) CHECK (cpf ~ '^[0-9]{11}$')")
	private String cpf;
	
	@OneToOne
    @JoinColumn(name = "user_id")
    private User bookstoreUser;
	
	@OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
	private List<Reservation> orders = new ArrayList<>();
	
	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Address> addresses = new ArrayList<>();
	
	public Person() {
		
	}

	public Person(
			Long id,
			String firstName,
			String lastName,
			LocalDate dateOfBirth,
			String gender,
			String phone,
			String nationality,
			String profilePictureUrl,
			String cpf,
			User user
		) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.phone = phone;
		this.nationality = nationality;
		this.profilePictureUrl = profilePictureUrl;
		this.cpf = cpf;
		this.bookstoreUser = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getProfilePictureUrl() {
		return profilePictureUrl;
	}

	public void setProfilePictureUrl(String profilePictureUrl) {
		this.profilePictureUrl = profilePictureUrl;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public User getUser() {
		return bookstoreUser;
	}

	public void setUser(User user) {
		this.bookstoreUser = user;
	}

	public List<Address> getAddresses() {
		return addresses;
	}
	
	public List<Reservation> getOrders() {
		return orders;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cpf, dateOfBirth, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		return Objects.equals(cpf, other.cpf) && Objects.equals(dateOfBirth, other.dateOfBirth)
				&& Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", dateOfBirth="
				+ dateOfBirth + ", gender=" + gender + ", phone=" + phone + ", nationality=" + nationality
				+ ", profilePictureUrl=" + profilePictureUrl + ", cpf=" + cpf + ", addresses=" + addresses + "]";
	}

}
