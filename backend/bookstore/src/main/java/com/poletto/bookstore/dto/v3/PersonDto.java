package com.poletto.bookstore.dto.v3;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.validator.constraints.URL;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder({"id"})
public class PersonDto extends RepresentationModel<PersonDto> implements Serializable {

	private static final long serialVersionUID = 3L;
	
	@JsonProperty("id")
	private Long key;
	
	@NotEmpty
	@Pattern(regexp = "^[A-Za-z]+$", message = "aceita apenas letras")
	@Size(min = 1, max = 30)
	private String firstName;
	
	@NotEmpty
	@Pattern(regexp = "^[A-Za-z]+(\\s[A-Za-z]+)*$", message = "aceita apenas letras")
	@Size(min = 1, max = 50)
	private String lastName;
	
	@NotNull
	@Past(message = "não pode ser data atual ou futura")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;
	
	@NotEmpty
	@Pattern(regexp = "^[A-Za-z]+$", message = "aceita apenas letras")
	@Size(min = 1, max = 20)
	private String gender;
	
	@NotEmpty
	@Pattern(regexp = "^\\d{11}$", message = "aceita apenas números no formato 99877776666")
	private String phone;
	
	@Size(min = 1, max = 20)
	private String nationality;
	
	@URL
	private String profilePictureUrl;
	
	@NotEmpty
	@Pattern(regexp = "^\\d{11}$", message = "aceita apenas números no formato 11122233344")
	private String cpf;
	
	@NotNull
	@JsonIncludeProperties({"id", "email", "userStatus"})
	private UserDto user;
	
	@Valid
	private List<AddressDto> addresses = new ArrayList<>();
	
	public PersonDto () {
		
	}

	public PersonDto(
			Long key,
			String firstName,
			String lastName,
			LocalDate dateOfBirth,
			String gender,
			String phone,
			String nationality,
			String profilePictureUrl,
			String cpf,
			UserDto user
		) {
		this.key = key;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.phone = phone;
		this.nationality = nationality;
		this.profilePictureUrl = profilePictureUrl;
		this.cpf = cpf;
		this.user = user;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
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

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public List<AddressDto> getAddresses() {
		return addresses;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(key, user);
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
		PersonDto other = (PersonDto) obj;
		return Objects.equals(key, other.key) && Objects.equals(user, other.user);
	}

	@Override
	public String toString() {
		return "PersonDto [key=" + key + ", firstName=" + firstName + ", lastname=" + lastName + ", dateOfBirth="
				+ dateOfBirth + ", gender=" + gender + ", phone=" + phone + ", nationality=" + nationality
				+ ", profilePictureUrl=" + profilePictureUrl + ", cpf=" + cpf + ", user=" + user + ", addresses="
				+ addresses + "]";
	}

}
