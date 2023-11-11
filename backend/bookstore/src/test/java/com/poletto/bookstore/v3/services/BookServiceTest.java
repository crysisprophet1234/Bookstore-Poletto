package com.poletto.bookstore.v3.services;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.filters;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import com.poletto.bookstore.dto.v2.BookDTOv2;
import com.poletto.bookstore.repositories.v3.BookRepository;
import com.poletto.bookstore.services.v3.BookService;
import com.poletto.bookstore.v3.mocks.BookMocks;
import com.poletto.bookstore.v3.mocks.UserAuthMocks;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class BookServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(BookServiceTest.class);
	
	@LocalServerPort
	private int serverPort;
	
	private final String serverBaseURI = "https://localhost";
	
	private Response response;
	private JsonPath jsonPath;
	private Map<String, String> json = new HashMap<>();
	
	@SpyBean
	private BookRepository bookRepository;
	
	@Autowired
	@InjectMocks
	private BookService bookService;
	
	private static BookDTOv2 bookDto;
 
    @BeforeEach
    public void setUp() {
    	
    	baseURI = serverBaseURI;
        port = serverPort;
        useRelaxedHTTPSValidation();
        filters(new RequestLoggingFilter(), new ResponseLoggingFilter()); 
	
    }
    
    @AfterEach
    public void tearDown() {
    	
    	response = null;
    	jsonPath = null;
    	json.clear();
    	
    }
    
    @Test
    @Order(1)
    void givenValidData_whenCreatingNewBook_thenCreateAndRetrieveBookDetails() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidData_whenCreatingNewBook_thenCreateAndRetrieveBookDetails()  =========>\n");
    	
    	bookDto = BookMocks.insertBookMockDto();
    	
    	response = given()
					.spec(UserAuthMocks.UserWithToken("operator@gmail.com"))
					.body(bookDto)
					.contentType(ContentType.JSON)
				  .when()
				  	.post("api/books/v3")
				  .then()
				  	.assertThat()
				  	.statusCode(201)
				  .and()
				  	.extract()
				  	.response();
    	
    	jsonPath = JsonPath.from(response.getBody().asString());
    	
    	Long bookId = jsonPath.getLong("id");
    	
    	assertEquals(bookDto.getName(), jsonPath.get("name"));
    	
    	assertEquals(bookDto.getReleaseDate().toString(), jsonPath.getString("releaseDate"));
    	
    	assertEquals(bookDto.getImgUrl(), jsonPath.get("imgUrl"));
    	
    	assertEquals(bookDto.getAuthor().getId(), jsonPath.getLong("author.id"));
    	
    	assertEquals(
    			bookDto.getCategories().stream().map(x -> x.getId()).collect(Collectors.toList()).toString(),
    			jsonPath.getList("categories.id").toString()
    	);
    	
    	assertEquals(jsonPath.getList("links").size(), 3);
    	
    	assertDoesNotThrow(() -> bookDto = bookService.findById(bookId));
    	
    	logger.info("test success, book was registered properly and the response provided the new book id along its details");	
    	
    }
    
    @Test
    @Order(2)
    void whenRequestingBookById_thenReceiveBookDetails() {
    	
    	logger.info("\n\n<=========  STARTING TEST whenRequestingBookById_thenReceiveBookDetails()  =========>\n");
    	
    	Long bookId = bookDto.getId();
    	
    	bookDto = bookService.findById(bookId);
    	
    	response = given()
    				.auth().none()
				  	.get("api/books/v3/{bookId}", bookId)
				  .then()
				  	.assertThat()
				  	.statusCode(200)
				  .and()
				  	.extract()
				  	.response();
    	
    	jsonPath = JsonPath.from(response.getBody().asString());
    	
    	assertEquals(bookDto.getId(), jsonPath.getLong("id"));
    	
    	assertEquals(bookDto.getName(), jsonPath.get("name"));
    	
    	assertEquals(bookDto.getReleaseDate().toString(), jsonPath.getString("releaseDate"));
    	
    	assertEquals(bookDto.getImgUrl(), jsonPath.get("imgUrl"));
    	
    	assertEquals(bookDto.getAuthor().getId(), jsonPath.getLong("author.id"));
    	
    	assertEquals(
    			bookDto.getCategories().stream().map(x -> x.getId()).collect(Collectors.toList()).toString(),
    			jsonPath.getList("categories.id").toString()
    	);
    	
    	assertEquals(jsonPath.getList("links").size(), 3);
    	
    	logger.info("test success, when a operator requested a book details by its id, the response properly provided the book details");
    	
    }
    
    @Test
    @Order(3)
    void givenPageParameters_whenRequestingBookPage_thenReceiveBookPage() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenPageParameters_whenRequestingBookPage_thenReceiveBookPage()  =========>\n");
    	
    	int pageSize = 3;
    	int pageNumber = 0;
    	
    	response = given()
				.auth().none()
				.queryParam("size", pageSize)
				.queryParam("page", pageNumber)
			  .when()
			  	.get("api/books/v3")
			  .then()
			  	.extract().response();
    	
    	jsonPath = JsonPath.from(response.getBody().asString());
    	
    	assertEquals(response.getStatusCode(), 200);
    	
    	assertFalse(jsonPath.getList("content").isEmpty());
    	
    	assertEquals(jsonPath.getInt("pageable.pageSize"), pageSize);
    	
    	assertEquals(jsonPath.getInt("pageable.pageNumber"), pageNumber);
    	
    	logger.info("test success, when a user requested an book page with defined page params, the response properly provided the page");
    	
    }
    
    @Test
    @Order(4)
    void givenValidData_whenUpdatingBookById_thenReceiveUpdatedBookDetails() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidData_whenUpdatingBookById_thenReceiveUpdatedBookDetails()  =========>\n");
    	
    	Long bookId = bookDto.getId();
    	
    	bookDto = bookService.findById(bookId);
    	
    	bookDto.setName("New Book Name");
    	
    	bookDto.setImgUrl("http://new_imageurl");
    	
    	response = given()
    			.spec(UserAuthMocks.UserWithToken("operator@gmail.com"))
    			.contentType(ContentType.JSON)
    			.body(bookDto)
			  	.put("api/books/v3/{bookId}", bookId)
			  .then()
			  	.assertThat()
			  	.statusCode(200)
			  .and()
			  	.extract()
			  	.response();
	
    	jsonPath = JsonPath.from(response.getBody().asString());
    	
    	assertEquals(bookDto.getId(), jsonPath.getLong("id"));
    	
    	assertEquals(bookDto.getName(), jsonPath.get("name"));
    	
    	assertEquals(bookDto.getReleaseDate().toString(), jsonPath.getString("releaseDate"));
    	
    	assertEquals(bookDto.getImgUrl(), jsonPath.get("imgUrl"));
    	
    	assertEquals(bookDto.getAuthor().getId(), jsonPath.getLong("author.id"));
    	
    	assertEquals(
    			bookDto.getCategories().stream().map(x -> x.getId()).collect(Collectors.toList()).toString(),
    			jsonPath.getList("categories.id").toString()
    	);
    	
    	assertEquals(jsonPath.getList("links").size(), 3);
    	
    	logger.info("test success, when a admin user updated a book details by its id, the response properly provided the updated details");
    	
    }
    
    @Test
    @Order(5)
    void givenExistingBook_whenDeletingBookById_thenReceivePositiveFeedback() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenExistingBook_whenDeletingBookById_thenReceivePositiveFeedback()  =========>\n");
    	
    	Long bookId = bookDto.getId();
    	
    	given()
			.spec(UserAuthMocks.AdminPrivilegesUser())
		.when()
			.delete("api/books/v3/{userId}", bookId)
		.then()
	  		.assertThat()
	  		.statusCode(204);
	
    	given()
			.spec(UserAuthMocks.AdminPrivilegesUser())
		.when()
			.get("api/books/v3/{userId}", bookId)
		.then()
			.assertThat()
			.statusCode(404)
		.and()
			.body("message", containsString("Resource BOOK not found. ID " + bookId));
	
    	logger.info("test success, when a admin user deleted a book by its id, the response properly provided feedback on the book deletion");
    	
    }
    
    @Test
    @Order(6)
    void givenBookOperationsRequests_whenUserDontHaveEnoughPrivileges_thenReceiveNotAuthorized() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenBookOperationsRequests_whenUserDontHaveEnoughPrivileges_thenReceiveNotAuthorized()  =========>\n");
    	
    	Long bookId = bookDto.getId();

    	given()
    	.auth().none()
    	.when()
    	.put("api/books/v3/{bookId}", bookId)
    	.then()
    	.assertThat()
    	.statusCode(401);

    	given()
    	.auth().none()
    	.when()
    	.delete("api/books/v3/{bookId}", bookId)
    	.then()
    	.assertThat()
    	.statusCode(401);
    	
    	RequestSpecification reqSpec = UserAuthMocks.UserWithToken("customer@gmail.com");

    	given()
    	.spec(reqSpec)
    	.when()
    	.put("api/books/v3/{bookId}", bookId)
    	.then()
    	.assertThat()
    	.statusCode(403);

    	given()
    	.spec(reqSpec)
    	.when()
    	.delete("api/books/v3/{bookId}", bookId)
    	.then()
    	.assertThat()
    	.statusCode(403);
    	
    	logger.info("test success, when a request was made without auth token the response was 401 UNAUTHORIZED "
    			  + "and when there was a token without OPERATOR or ADMIN privileges then the response was 403 FORBIDDEN");
    	
    }

}
