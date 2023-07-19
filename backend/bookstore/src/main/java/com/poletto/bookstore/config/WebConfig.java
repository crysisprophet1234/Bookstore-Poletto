package com.poletto.bookstore.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.poletto.bookstore.serialization.converter.YamlJacksonToHttpMessageConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private static final MediaType MEDIA_TYPE_APPLICATION_YML = MediaType.valueOf("application/x-yaml");

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
