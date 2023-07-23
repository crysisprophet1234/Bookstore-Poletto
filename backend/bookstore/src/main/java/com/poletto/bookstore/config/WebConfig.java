package com.poletto.bookstore.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poletto.bookstore.converter.CustomObjectMapper;
import com.poletto.bookstore.serialization.converter.YamlJacksonToHttpMessageConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private static final MediaType MEDIA_TYPE_APPLICATION_YML = MediaType.valueOf("application/x-yaml");
	
	@Bean(name = "jsonMapper")
	@Primary
	ObjectMapper jsonMapper() {
		return new CustomObjectMapper();
	}
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
	  converters.add(new MappingJackson2HttpMessageConverter(jsonMapper()));
	}
	
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new YamlJacksonToHttpMessageConverter());
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

		configurer
			.favorParameter(false)
			.ignoreAcceptHeader(false)
			.useRegisteredExtensionsOnly(false)
				.mediaType("json", MediaType.APPLICATION_JSON)
				.mediaType("xml", MediaType.APPLICATION_XML)
				.mediaType("text/plain", MediaType.TEXT_PLAIN)
				.mediaType("x-yaml", MEDIA_TYPE_APPLICATION_YML) //TODO quando existe data no response, o yaml n parsa
				//.mediaType("format-data", MediaType.form) //verificar form url encoded
			.defaultContentType(MediaType.APPLICATION_JSON);

	}

}
