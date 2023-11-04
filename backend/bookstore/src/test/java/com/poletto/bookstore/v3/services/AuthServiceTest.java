package com.poletto.bookstore.v3.services;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.filters;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
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

import com.poletto.bookstore.config.JwtService;
import com.poletto.bookstore.dto.v2.UserDTOv2;
import com.poletto.bookstore.repositories.v3.AuthRepository;
import com.poletto.bookstore.repositories.v3.UserRepository;
import com.poletto.bookstore.services.v2.EmailService;
import com.poletto.bookstore.services.v3.UserService;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class AuthServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthServiceTest.class);
	
	@LocalServerPort
	private int serverPort;
	
	private final String serverBaseURI = "https://localhost";
	
	@Autowired
	private JwtService jwtService;
	
	@SpyBean
	private EmailService emailService;
	
	@SpyBean
	private UserRepository userRepository;
	
	@SpyBean
	private AuthRepository authRepository;

	@Autowired
	@InjectMocks
	private UserService userService;
	
	private Response response;
	private JsonPath jsonPath;
	private Map<String, String> json = new HashMap<>();
	
	private static UserDTOv2 userDto;
 
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
    void givenValidCredentials_whenCreatingNewAccount_thenCreateAndProvideDetails() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidCredentials_whenCreatingNewAccount_thenCreateAndProvideDetails()  =========>\n");
    	
    	json.put("firstname", "newfirstname");
		json.put("lastname", "newlastname");
		json.put("email", "newemail@mail.com");
		json.put("password", "newpsw12");
		
		response = given()
			.contentType(ContentType.JSON)
			.body(new JSONObject(json).toString())
		.when()
			.post("api/auth/v3/register")
			.then()
			.extract().response();
		
		jsonPath = JsonPath.from(response.getBody().asString());
		
		assertEquals(response.getStatusCode(), 201);
		
		Long userId = jsonPath.getLong("id");
		
		assertNotNull(userId);
		
		assertEquals(jsonPath.get("firstname"), json.get("firstname"));
		
		assertEquals(jsonPath.get("lastname"), json.get("lastname"));
		
		assertEquals(jsonPath.get("email"), json.get("email"));
		
		assertNull(jsonPath.get("password"));
		
		assertEquals(jsonPath.getList("links.href").size(), 3);
		
		assertDoesNotThrow(() -> userDto = userService.findById(userId));
		
		verify(authRepository, times(1)).save(any());
		
		verify(emailService, times(1)).sendEmailFromTemplate(
				userDto.getEmail(),
				"Confirmação de criação de conta", userDto.getFirstname() + " " + userDto.getLastname()
		);
		
		logger.info("test success, user was registered properly and the response provided the new user id along user details");
		
    }
	
	@Test
	@Order(2)
	void givenValidUserCredentials_whenAuthenticating_thenReceiveValidToken() {
		
		logger.info("\n\n<=========  STARTING TEST givenValidUserCredentials_whenAuthenticating_thenReceiveValidToken()  =========>\n");
		
		json.put("email", userDto.getEmail());
		json.put("password", "newpsw12");
		
		response = given()
			.contentType(ContentType.JSON)
			.body(new JSONObject(json).toString())
		.when()
			.post("api/auth/v3/authenticate")
			.then().extract().response();
		
		jsonPath = JsonPath.from(response.getBody().asString());
		
		assertEquals(response.getStatusCode(), 200);

		assertEquals(jsonPath.getLong("id"), userDto.getId());
		
		assertEquals(jsonPath.get("email"), userDto.getEmail());
		
		assertEquals(jsonPath.get("firstname"), userDto.getFirstname());
		
		assertEquals(jsonPath.get("lastname"), userDto.getLastname());
		
		assertEquals(jsonPath.getList("links.href").size(), 3);
		
		assertEquals(jsonPath.getList("roles").size(), 1);
		
		assertEquals(jsonPath.get("roles[0].authority"), "ROLE_CUSTOMER");
		
		assertEquals(
				jwtService.extractUsername(jsonPath.getString("token")),
				userDto.getEmail(),
				"username on token didnt got setted properly"
		);
		
		verify(authRepository, times(1)).findByEmail(userDto.getEmail());
		
		logger.info("test success, user was authenticated properly and the response provided the token along user details");
		
	}
	
	@Test
	@Order(3)
    void givenAlreadyRegisteredEmail_whenCreatingNewAccount_thenReceive() {
		
		logger.info("\n\n<=========  STARTING TEST givenAlreadyRegisteredEmail_whenCreatingNewAccount_thenReceive()  =========>\n");
		
		json.put("firstname", "newfirstname");
		json.put("lastname", "newlastname");
		json.put("email", "newemail@mail.com");
		json.put("password", "newpsw12");
		
		given()
			.contentType(ContentType.JSON)
			.body(new JSONObject(json).toString())
		.when()
			.post("api/auth/v3/register")
		.then()
		.assertThat()
			.statusCode(409)
		.and()
			.body("message", containsString("Email provided is already registered"));
		
		logger.info("test success, when the user provides a already registered email the response provideds a conflict status and message explaining");
		
	}
	
	@Test
    void givenInvalidCredentials_whenCreatingNewAccount_thenReceiveUnprocessableEntity() {
		
		logger.info("\n\n<=========  STARTING TEST givenInvalidCredentials_whenCreatingNewAccount_thenReceiveUnprocessableEntity()  =========>\n");
		
		json.put("firstname", "newfirstname");
		json.put("lastname", "newlastname");
		json.put("email", "invalid_email");
		json.put("password", "pass123");
		
		given()
			.contentType(ContentType.JSON)
			.body(new JSONObject(json).toString())
		.when()
			.post("api/auth/v3/register").then()
			.assertThat()
			.statusCode(422)
			.body("message", containsString("campo [email] deve ser um endereço de e-mail bem formado"));
		
		json.put("email", "valid@email.com");
		json.put("password", "invalid_password");
		
		given()
			.contentType(ContentType.JSON)
			.body(new JSONObject(json).toString())
		.when()
			.post("api/auth/v3/register").then()
			.assertThat()
			.statusCode(422)
			.body("message", containsString("campo [password] deve conter entre 6 e 8 caracteres"));
		
		logger.info("test success, new users were not created and the message provided explained what was wrong");
		
	}
	
	@Test
	void givenNonExistingUserCredentials_whenAuthenticating_thenReceiveBadCredentials() {
		
		logger.info("\n\n<=========  STARTING TEST givenNonExistingUserCredentials_whenAuthenticating_thenReceiveBadCredentials()  =========>\n");
		
		json.put("email", "nonexisting_user");
		json.put("password", "nonexisting_psw");
		
		given()
			.contentType(ContentType.JSON)
			.body(new JSONObject(json).toString())
		.when()
			.post("api/auth/v3/authenticate").then()
			.assertThat()
			.statusCode(401);
		
		logger.info("test success, when user credentials didnt matched any account created a bad credentials status was returned");
		
	}
	
}
