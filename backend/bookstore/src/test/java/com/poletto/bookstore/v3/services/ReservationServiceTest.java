package com.poletto.bookstore.v3.services;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.filters;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import com.poletto.bookstore.dto.v2.BookDTOv2;
import com.poletto.bookstore.dto.v2.ReservationDTOv2;
import com.poletto.bookstore.entities.enums.BookStatus;
import com.poletto.bookstore.entities.enums.ReservationStatus;
import com.poletto.bookstore.repositories.v3.BookRepository;
import com.poletto.bookstore.repositories.v3.ReservationRepository;
import com.poletto.bookstore.services.v3.BookService;
import com.poletto.bookstore.services.v3.ReservationService;
import com.poletto.bookstore.v3.mocks.BookMocks;
import com.poletto.bookstore.v3.mocks.ReservationMocks;
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
public class ReservationServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ReservationServiceTest.class);
	
	@LocalServerPort
	private int serverPort;
	
	private final String serverBaseURI = "https://localhost";
	
	private Response response;
	private JsonPath jsonPath;
	private Map<String, String> json = new HashMap<>();
	
	@Mock
	private BookRepository bookRepository;
	
	@SpyBean
	private ReservationRepository reservationRepository;
	
	@Autowired
	@InjectMocks
	private BookService bookService;
	
	@Autowired
	@InjectMocks
	private ReservationService reservationService;
	
	private static ReservationDTOv2 reservationDto;
	
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
    void givenValidData_whenCreatingNewReservation_thenCreateAndRetrieveReservationDetails() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidData_whenCreatingNewReservation_thenCreateAndRetrieveReservationDetails()  =========>\n");
    		
    	bookDto = bookService.insert(BookMocks.insertBookMockDto());
        	
        reservationDto = ReservationMocks.insertReservationMockDto(bookDto.getId());
	
    	response = given()
					.spec(UserAuthMocks.UserWithToken("operator@gmail.com"))
					.body(reservationDto)
					.contentType(ContentType.JSON)
				.when()
			  		.post("api/reservations/v3")
			  	.then()
				  	.assertThat()
				  	.statusCode(201)
				  	.and()
				  	.extract()
				  	.response();
	
    	jsonPath = JsonPath.from(response.getBody().asString());
    	
    	Long reservationId = jsonPath.getLong("id");
    	
    	assertEquals(reservationDto.getClient().getId(), jsonPath.getLong("client.id"));
    	
    	assertEquals(ReservationStatus.IN_PROGRESS, ReservationStatus.valueOf(jsonPath.getString("status")));
    	
    	assertEquals(reservationDto.getWeeks(), jsonPath.getInt("weeks"));
    	
    	assertEquals(LocalDate.now().plusWeeks(reservationDto.getWeeks()).toString(), jsonPath.get("devolution"));
    	
    	assertEquals(reservationDto.getBooks().get(0).getId(), jsonPath.getLong("books[0].id"));
    	
    	assertEquals(BookStatus.BOOKED, BookStatus.valueOf(jsonPath.getString("books[0].status")));
    	
    	assertEquals(jsonPath.getList("links").size(), 2);
    	
    	assertDoesNotThrow(() -> reservationDto = reservationService.findById(reservationId));
    	
    	logger.info("test success, reservation was registered properly and the response provided the new reservation id along its details");
    	 	
    }
    
    @Test
    @Order(2)
    void givenAlreadyReservedBook_whenCreatingNewReservation_thenReceiveInvalidStatusResponse () {
    	
    	logger.info("\n\n<=========  STARTING TEST givenAlreadyReservedBook_whenCreatingNewReservation_thenReceiveInvalidStatusResponse()  =========>\n");
    	
    	Long reservationId = reservationDto.getId();
    	
    	reservationDto = ReservationMocks.insertReservationMockDto(reservationDto.getBooks().get(0).getId());
    	
    	given()
			.spec(UserAuthMocks.UserWithToken("operator@gmail.com"))
			.body(reservationDto)
			.contentType(ContentType.JSON)
		.when()
	  		.post("api/reservations/v3")
	  	.then()
		  	.assertThat()
		  	.statusCode(400)
		.and()
			.body("message", is("Invalid status for book ID " + reservationDto.getBooks().get(0).getId()));
    	
    	assertDoesNotThrow(() -> reservationDto = reservationService.findById(reservationId));
    	
    	logger.info("test success, reservation wasnt registered when the book passed is already reserved, the response provided the details");
    	
    }
    
    @Test
    @Order(3)
    void whenRequestingReservationById_thenReceiveReservationDetails() throws ParseException {
    	
    	logger.info("\n\n<=========  STARTING TEST whenRequestingReservationById_thenReceiveReservationDetails()  =========>\n");
    	
    	Long reservationId = reservationDto.getId();
    	
    	bookDto = bookService.findById(reservationId);
    	
    	response = given()
    			.spec(UserAuthMocks.UserWithToken("operator@gmail.com"))
			  	.get("api/reservations/v3/{reservationId}", reservationId)
			  .then()
			  	.assertThat()
			  	.statusCode(200)
			  .and()
			  	.extract()
			  	.response();
	
    	jsonPath = JsonPath.from(response.getBody().asString());
    	
    	assertEquals(reservationDto.getId(), jsonPath.getLong("id"));
    	
    	assertEquals(
    			reservationDto.getDevolution().toString(),
    			jsonPath.getString("devolution")
    	);
    	
    	assertEquals(reservationDto.getWeeks(), jsonPath.getInt("weeks"));
    	
    	assertEquals(reservationDto.getStatus(), ReservationStatus.valueOf(jsonPath.get("status")));
    	
    	assertEquals(reservationDto.getClient().getId(), jsonPath.getLong("client.id"));
    	
    	assertEquals(
    			reservationDto.getBooks().stream().map(x -> x.getId()).collect(Collectors.toList()).toString(),
    			jsonPath.getList("books.id").toString()
    	);
    	
    	assertEquals(jsonPath.getList("links").size(), 2);
    	
    	logger.info("test success, when a operator requested a reservation details by its id, the response properly provided the reservation details");
    	
    }
    
    @Test
    @Order(4)
    void givenPageParameters_whenRequestingReservationPage_thenReceiveReservationPage() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenPageParameters_whenRequestingReservationPage_thenReceiveReservationPage()  =========>\n");
    	
    	int pageSize = 3;
    	int pageNumber = 0;
    	
    	response = given()
    			.spec(UserAuthMocks.UserWithToken("operator@gmail.com"))
				.queryParam("size", pageSize)
				.queryParam("page", pageNumber)
			  .when()
			  	.get("api/reservations/v3")
			  .then()
			  	.extract().response();
    	
    	jsonPath = JsonPath.from(response.getBody().asString());
    	
    	assertEquals(response.getStatusCode(), 200);
    	
    	assertFalse(jsonPath.getList("content").isEmpty());
    	
    	assertEquals(jsonPath.getInt("pageable.pageSize"), pageSize);
    	
    	assertEquals(jsonPath.getInt("pageable.pageNumber"), pageNumber);
    	
    	given()
			.spec(UserAuthMocks.UserWithToken("operator@gmail.com"))
			.queryParam("size", pageSize)
			.queryParam("page", pageNumber)
			.queryParam("book", 2)
		.when()
	  		.get("api/reservations/v3")
	  	.then()
	  		.statusCode(200)
	  		.body("content", IsEmptyCollection.empty());
    	
    	given()
			.spec(UserAuthMocks.UserWithToken("operator@gmail.com"))
			.queryParam("size", pageSize)
			.queryParam("page", pageNumber)
			.queryParam("client", 2)
		.when()
  			.get("api/reservations/v3")
  		.then()
  			.statusCode(200)
  			.body("content", IsEmptyCollection.empty());
    	
    	given()
			.spec(UserAuthMocks.UserWithToken("operator@gmail.com"))
			.queryParam("size", pageSize)
			.queryParam("page", pageNumber)
			.queryParam("status", "finished")
		.when()
			.get("api/reservations/v3")
		.then()
			.statusCode(200)
			.body("content", IsEmptyCollection.empty());
    	
    	logger.info("test success, when a user requested an reservation page with defined page params, the response properly provided the page");
    	
    }
    
    @Test
    @Order(5)
    void givenExistingAndInProgressReservation_whenReturningReservation_thenReturnAndRespondWithPositiveFeedback() {
    	
    	logger.info("\n\n<=========  STARTING TEST whenRequestingReservationById_thenReceiveReservationDetails()  =========>\n");
    	
    	Long reservationId = reservationDto.getId();
    	
    	Long bookId = reservationDto.getBooks().get(0).getId();
    	
    	RequestSpecification reqSpec = UserAuthMocks.UserWithToken("operator@gmail.com");
    	
    	given()
			.spec(reqSpec)
			.put("api/reservations/v3/return/{reservationId}", reservationId)
		.then()
	  		.assertThat()
	  		.statusCode(204);
    	
    	given()
			.spec(reqSpec)
		  	.get("api/reservations/v3/{reservationId}", reservationId)
		.then()
		  	.assertThat()
		  	.statusCode(200)
		.and()
		  	.body("status", is(ReservationStatus.FINISHED.toString()))
		  	.body("books[0].status", is(BookStatus.AVAILABLE.toString()));
    	
    	given()
			.auth().none()
			.get("api/books/v3/{bookId}", bookId)
		.then()
  			.assertThat()
  			.statusCode(200)
  		.and()
  			.body("status", is(BookStatus.AVAILABLE.toString()));
    	
    	given()
			.spec(reqSpec)
			.put("api/reservations/v3/return/{reservationId}", reservationId)
		.then()
  			.assertThat()
  			.statusCode(400)
  		.and()
  			.body("message", is("Invalid status for reservation ID " + reservationId));
    	
    	logger.info("test success, when a operator returned a reservation by its id, the response properly provided the positive feedback on the return");

    }
    
    @Test
    @Order(6)
    void givenReservationOperationsRequests_whenUserDontHaveEnoughPrivileges_thenReceiveNotAuthorized() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenReservationOperationsRequests_whenUserDontHaveEnoughPrivileges_thenReceiveNotAuthorized()  =========>\n");
    	
    	Long reservationId = reservationDto.getId();
    	
    	given()
    	.auth().none()
    	.when()
    	.get("api/reservations/v3")
    	.then()
    	.assertThat()
    	.statusCode(401);
    	
    	given()
    	.auth().none()
    	.when()
    	.get("api/reservations/v3/{reservationId}", reservationId)
    	.then()
    	.assertThat()
    	.statusCode(401);

    	given()
    	.auth().none()
    	.when()
    	.post("api/reservations/v3")
    	.then()
    	.assertThat()
    	.statusCode(401);
    	
    	given()
    	.auth().none()
    	.when()
    	.put("api/reservations/v3/return/{reservationId}", reservationId)
    	.then()
    	.assertThat()
    	.statusCode(401);
    	
    	RequestSpecification reqSpec = UserAuthMocks.UserWithToken("customer@gmail.com");

    	given()
    	.spec(reqSpec)
    	.when()
    	.get("api/reservations/v3")
    	.then()
    	.assertThat()
    	.statusCode(403);
    	
    	given()
    	.spec(reqSpec)
    	.when()
    	.get("api/reservations/v3/{reservationId}", reservationId)
    	.then()
    	.assertThat()
    	.statusCode(403);

    	given()
    	.spec(reqSpec)
    	.when()
    	.post("api/reservations/v3")
    	.then()
    	.assertThat()
    	.statusCode(403);
    	
    	given()
    	.spec(reqSpec)
    	.when()
    	.put("api/reservations/v3/return/{reservationId}", reservationId)
    	.then()
    	.assertThat()
    	.statusCode(403);
    	
    	logger.info("test success, when a request was made without auth token the response was 401 UNAUTHORIZED "
    			  + "and when there was a token without OPERATOR or ADMIN privileges then the response was 403 FORBIDDEN");
    	   	
    }

}