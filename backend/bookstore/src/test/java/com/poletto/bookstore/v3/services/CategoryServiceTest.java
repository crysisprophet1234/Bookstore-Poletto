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

import com.poletto.bookstore.dto.v2.CategoryDTOv2;
import com.poletto.bookstore.repositories.v3.CategoryRepository;
import com.poletto.bookstore.services.v3.CategoryService;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class CategoryServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(CategoryServiceTest.class);

	@LocalServerPort
	private int serverPort;

	private final String serverBaseURI = "https://localhost";

	@Mock
	private CategoryRepository categoryRepository;

	@Autowired
	@InjectMocks
	private CategoryService categoryService;

	private static CategoryDTOv2 categoryDto;

	@BeforeEach
	public void setUp() {

		baseURI = serverBaseURI;
		port = serverPort;
		useRelaxedHTTPSValidation();
		filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

	}

	@Test
	@Order(1)
	void givenExistingId_whenRequestingCategoryById_thenReceiveCategoryDetails() {

		logger.info("\n\n<=========  STARTING TEST givenExistingId_whenRequestingCategoryById_thenReceiveCategoryDetails()  =========>\n");

		Long categoryId = 1L;

		categoryDto = categoryService.findById(categoryId);

		given()
			.auth().none()
			.get("api/categories/v3/{categoryId}", categoryId)
		.then()
			.assertThat()
			.statusCode(200)		
		.and()
			.body("id", is(categoryDto.getId().intValue()))
			.body("name", is(categoryDto.getName()))
			.body("links[0].rel", containsString(categoryDto.getLink("BOOKS WITH THIS CATEGORY").get().getRel().toString()))
			.body("links[0].href", containsString(categoryDto.getLink("BOOKS WITH THIS CATEGORY").get().getHref()))
			.body("links[0].type", containsString(categoryDto.getLink("BOOKS WITH THIS CATEGORY").get().getType()));
		
		given()
			.auth().none()
			.get("api/categories/v3/9999")
		.then()
			.assertThat()
			.statusCode(404)
		.and()
			.body("message", is("Resource CATEGORY not found. ID 9999"));

		logger.info("test success, when a user requested a category details by its id, the response properly provided the category details");

	}

	@Test
	@Order(2)
	void whenRequestingAllCategories_thenReceiveListOfAllCategories() {

		logger.info("\n\n<=========  STARTING TEST whenRequestingAllCategories_thenReceiveListOfAllCategories()  =========>\n");
		
		given()
			.auth().none()
			.get("api/categories/v3")
		.then()
			.assertThat()
			.statusCode(200)
		.and()
			.body("$", is(not(IsEmptyCollection.empty())));

	}

}