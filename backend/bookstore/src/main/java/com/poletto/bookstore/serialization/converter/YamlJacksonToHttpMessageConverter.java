package com.poletto.bookstore.serialization.converter;

import java.time.format.DateTimeFormatter;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class YamlJacksonToHttpMessageConverter extends AbstractJackson2HttpMessageConverter {
	
	static JavaTimeModule javaTimeModule = (JavaTimeModule) new JavaTimeModule().addSerializer(java.time.LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

	public YamlJacksonToHttpMessageConverter() {

		super(new YAMLMapper()
				.registerModule(javaTimeModule)
				.setSerializationInclusion
					(JsonInclude.Include.NON_NULL), 
					MediaType.parseMediaType("application/x-yaml"));
	}

}
