package com.poletto.bookstore.converter;

import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class CustomObjectMapper extends ObjectMapper {

	private static final long serialVersionUID = 1L;

	public CustomObjectMapper() {
		super();
		
		setSerializationInclusion(JsonInclude.Include.NON_NULL);
		setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		
		JsonMapper.builder()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
			.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
			.build();
		
		 JavaTimeModule javaTimeModule = new JavaTimeModule();
	        javaTimeModule.addSerializer(
	                java.time.LocalDate.class,
	                new LocalDateSerializer(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
	        );
			
		registerModule(javaTimeModule);
		
	}
	
	@Override
    public CustomObjectMapper copy() {
        return new CustomObjectMapper();
    }
	
}
