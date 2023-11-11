package com.poletto.bookstore.v3.services;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.filters;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import com.poletto.bookstore.dto.v2.AuthorDTOv2;
import com.poletto.bookstore.repositories.v3.AuthorRepository;
import com.poletto.bookstore.services.v3.AuthorService;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class AuthorServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthorServiceTest.class);

	@LocalServerPort
	private int serverPort;

	private final String serverBaseURI = "https://localhost";

	@Mock
	private AuthorRepository authorRepository;

	@Autowired
	@InjectMocks
	private AuthorService authorService;

	private static AuthorDTOv2 authorDto;

	@BeforeEach
	public void setUp() {

		baseURI = serverBaseURI;
		port = serverPort;
		useRelaxedHTTPSValidation();
		filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

	}

	@Test
	@Order(1)
	void givenExistingId_whenRequestingAuthorById_thenReceiveAuthorDetails() {
		
		logger.info("\n\n<=========  STARTING TEST givenExistingId_whenRequestingAuthorById_thenReceiveAuthorDetails()  =========>\n");

		Long authorId = 1L;

		authorDto = authorService.findById(authorId);

		given()
			.auth().none()
			.get("api/authors/v3/{authorId}", authorId)
		.then()
			.assertThat()
			.statusCode(200)		
		.and()
			.body("id", is(authorDto.getId().intValue()))
			.body("name", is(authorDto.getName()))
			.body("links[0].rel", containsString(authorDto.getLink("self").get().getRel().toString()))
			.body("links[0].href", containsString(authorDto.getLink("self").get().getHref()))
			.body("links[0].type", containsString(authorDto.getLink("self").get().getType()))
			.body("links[1].rel", containsString(authorDto.getLink("BOOKS BY AUTHOR").get().getRel().toString()))
			.body("links[1].href", containsString(authorDto.getLink("BOOKS BY AUTHOR").get().getHref()))
			.body("links[1].type", containsString(authorDto.getLink("BOOKS BY AUTHOR").get().getType()));
		
		given()
			.auth().none()
			.get("api/authors/v3/9999")
		.then()
			.assertThat()
			.statusCode(404)
		.and()
			.body("message", is("Resource AUTHOR not found. ID 9999"));

		logger.info("test success, when a user requested a author details by its id, the response properly provided the authors details");
		
	}
	
	@Test
	@Order(2)
	void whenRequestingAllAuthors_thenReceiveListOfAllAuthors() {

		logger.info("\n\n<=========  STARTING TEST whenRequestingAllAuthors_thenReceiveListOfAllAuthors()  =========>\n");
		
		given()
			.auth().none()
			.get("api/authors/v3")
		.then()
			.assertThat()
			.statusCode(200)
		.and()
			.body("$", is(not(IsEmptyCollection.empty())));

	}

}
