package com.poletto.bookstore.dto.v3;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@JsonPropertyOrder({"id"})
public class AddressDto implements Serializable {

	private static final long serialVersionUID = 3L;
	
	@JsonProperty("id")
	private Long key;

	@NotNull
	@Pattern(regexp = "^\\d{8}$", message = "aceita apenas números no formato 11222333")
	private String cep;

	@NotNull
    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "aceita apenas números e letras")
	@Length(min = 2, max = 50, message = "deve possuir entre 2 e 50 caracteres")
	private String addressName;
	
    @NotNull
	@Max(9999)
	@Min(1)
	private Integer number;
	
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "aceita apenas números e letras")
	@Length(min = 5, max = 100, message = "deve possuir entre 5 e 100 caracteres")
	private String complement;
	
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "aceita apenas números e letras")
	@Length(min = 2, max = 50, message = "deve possuir entre 2 e 50 caracteres")
	private String district;
    
    @NotNull
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "aceita apenas letras")
	@Length(min = 2, max = 50, message = "deve possuir entre 2 e 50 caracteres")
	private String city;
    
    @NotNull
    @Pattern(
		regexp = "^(AC|AL|AP|AM|BA|CE|DF|ES|GO|MA|MT|MS|MG|PA|PB|PR|PE|PI|RJ|RN|RS|RO|RR|SC|SP|SE|TO)$",
		message = "deve ser um código válido de UF"
	)
    private String state;
    
    public AddressDto() {
    	
    }

	public AddressDto(
			Long id,
			String cep,
			String addressName,
			Integer number,
			String complement,
			String district,
			String city,
			String state
		) {
		this.key = id;
		this.cep = cep;
		this.addressName = addressName;
		this.number = number;
		this.complement = complement;
		this.district = district;
		this.city = city;
		this.state = state;
	}

	public Long getId() {
		return key;
	}

	public void setId(Long id) {
		this.key = id;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(cep, key);
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
		AddressDto other = (AddressDto) obj;
		return Objects.equals(cep, other.cep) && Objects.equals(key, other.key);
	}

	@Override
	public String toString() {
		return "AddressDto [id=" + key + ", cep=" + cep + ", addressName=" + addressName + ", number=" + number
				+ ", complement=" + complement + ", district=" + district + ", city=" + city + ", state=" + state + "]";
	}  

}
