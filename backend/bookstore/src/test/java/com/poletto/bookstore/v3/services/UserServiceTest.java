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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
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

import com.poletto.bookstore.dto.v2.RoleDTOv2;
import com.poletto.bookstore.dto.v2.UserAuthDTOv2;
import com.poletto.bookstore.dto.v2.UserDTOv2;
import com.poletto.bookstore.repositories.v3.UserRepository;
import com.poletto.bookstore.services.v3.UserService;
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
public class UserServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);
	
	@LocalServerPort
	private int serverPort;
	
	private final String serverBaseURI = "https://localhost";
	
	private Response response;
	private JsonPath jsonPath;
	private Map<String, String> json = new HashMap<>();
	
	@SpyBean
	private UserRepository userRepository;
	
	@Autowired
	@InjectMocks
	private UserService userService;
	
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
    void whenRequestingUserById_thenReceiveUserDetails() {
    	
    	logger.info("\n\n<=========  STARTING TEST whenRequestingUserById_thenReceiveUserDetails()  =========>\n");
    	
    	Long userId = 1L;
    	
    	userDto = userService.findById(userId);
    	
    	response = given()
    				.spec(UserAuthMocks.AdminPrivilegesUser())
    			  .when()
    			  	.get("api/users/v3/{userId}", userId)
    			  .then()
    			  	.extract().response();
    	
    	jsonPath = JsonPath.from(response.getBody().asString());
		
		assertEquals(response.getStatusCode(), 200);
		
		assertEquals(userDto.getId(), jsonPath.getLong("id"));
		
		assertEquals(userDto.getEmail(), jsonPath.get("email"));
		
		assertEquals(userDto.getFirstname(), jsonPath.get("firstname"));
		
		assertEquals(userDto.getLastname(), jsonPath.get("lastname"));
		
		assertEquals(userDto.getRoles().toString(), jsonPath.getList("roles").toString());
		
		assertEquals(userDto.getLinks().toList().size(), (jsonPath.getList("links").size()));
		
		assertDoesNotThrow(() -> userDto = userService.findById(userId));
		
		logger.info("test success, when a admin user requested a user details by its id, the response properly provided the details");
    	
    }
    
    @Test
    @Order(2)
    void givenPageParameters_whenRequestingUserPage_thenReceiveUserPage() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenPageParameters_whenRequestingUserPage_thenReceiveUserPage()  =========>\n");
    	
    	int pageSize = 3;
    	int pageNumber = 0;
    	
    	response = given()
				.spec(UserAuthMocks.AdminPrivilegesUser())
				.queryParam("size", pageSize)
				.queryParam("page", pageNumber)
			  .when()
			  	.get("api/users/v3")
			  .then()
			  	.extract().response();
    	
    	jsonPath = JsonPath.from(response.getBody().asString());
    	
    	assertEquals(response.getStatusCode(), 200);
    	
    	assertFalse(jsonPath.getList("content").isEmpty());
    	
    	assertEquals(jsonPath.getInt("pageable.pageSize"), pageSize);
    	
    	assertEquals(jsonPath.getInt("pageable.pageNumber"), pageNumber);
    	
    	logger.info("test success, when a admin user requested an user page with defined page params, the response properly provided the page");
    	
    }
    
    @Test
    @Order(3)
    void givenValidData_whenUpdatingUserById_thenReceiveUpdatedUserDetails() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidData_whenUpdatingUserById_thenReceiveUpdatedUserDetails()  =========>\n");
    	
    	Long userId = userDto.getId();
    	
    	UserAuthDTOv2 userAuthDto = new UserAuthDTOv2(
    			userService.findById(userId),
    			"psw1234",
    			null
    	);
    	
    	userAuthDto.setFirstname("NewFirstName");
    	
    	userAuthDto.getRoles().add(new RoleDTOv2(2L, "ROLE_OPERATOR"));

    	response = given()
				.spec(UserAuthMocks.AdminPrivilegesUser())
				.body(userAuthDto)
				.contentType(ContentType.JSON)
			  .when()
			  	.put("api/users/v3/{userId}", userId)
			  .then()
			  	.extract().response();
    	
    	jsonPath = JsonPath.from(response.getBody().asString());
    	
    	assertEquals(200, response.getStatusCode());
    	
    	assertEquals(jsonPath.getLong("id"), userId);
    	
    	assertEquals(jsonPath.get("firstname"), "NewFirstName");
		
		assertEquals(jsonPath.get("lastname"), userAuthDto.getLastname());
		
		assertEquals(jsonPath.get("email"), userAuthDto.getEmail());
		
		assertEquals(jsonPath.getList("roles").toString(), userAuthDto.getRoles().toString());
		
		assertEquals(jsonPath.getList("links").size(), userService.findById(userId).getLinks().toList().size());
    	
		logger.info("test success, when a admin user updated the user details by its id, the response properly provided the updated details");
		
    }

    @Test
    @Order(4)
    void givenExistingUser_whenDeletingUserById_thenReceivePositiveFeedback() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenExistingUser_whenDeletingUserById_thenReceivePositiveFeedback()  =========>\n");
    	
    	Long userId = userDto.getId();
    	
    	given()
			.spec(UserAuthMocks.AdminPrivilegesUser())
		.when()
	  		.delete("api/users/v3/{userId}", userId)
	  	.then()
	  		.assertThat()
	  		.statusCode(204);
    	
    	given()
			.spec(UserAuthMocks.AdminPrivilegesUser())
		.when()
  			.get("api/users/v3/{userId}", userId)
  		.then()
  			.assertThat()
  			.statusCode(404)
  			.and()
  			.body("message", containsString("Resource USER not found. ID " + userId));
    	
    	logger.info("test success, when a admin user deleted a user by id, the response properly provided feedback on the user deletion");
    	
    }
    
    @Test
    @Order(5)
    void givenUserOperationsRequests_whenUserDontHaveAdminPrivileges_thenReceiveNotAuthorized() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenUserOperationsRequests_whenUserDontHaveAdminPrivileges_thenReceiveNotAuthorized()  =========>\n");
    	
    	Long userId = userDto.getId();
    	
    	given()
    	.auth().none()
    	.when()
    	.get("api/users/v3")
    	.then()
    	.assertThat()
    	.statusCode(401);
    	
    	given()
    	.auth().none()
    	.when()
    	.get("api/users/v3/{userId}", userId)
    	.then()
    	.assertThat()
    	.statusCode(401);

    	given()
    	.auth().none()
    	.when()
    	.put("api/users/v3/{userId}", userId)
    	.then()
    	.assertThat()
    	.statusCode(401);

    	given()
    	.auth().none()
    	.when()
    	.delete("api/users/v3/{userId}", userId)
    	.then()
    	.assertThat()
    	.statusCode(401);
    	
    	RequestSpecification reqSpec = UserAuthMocks.UserWithToken("operator@gmail.com");
    	
    	given()
    	.spec(reqSpec)
    	.when()
    	.get("api/users/v3")
    	.then()
    	.assertThat()
    	.statusCode(403);
    	
    	given()
    	.spec(reqSpec)
    	.when()
    	.get("api/users/v3/{userId}", userId)
    	.then()
    	.assertThat()
    	.statusCode(403);

    	given()
    	.spec(reqSpec)
    	.when()
    	.put("api/users/v3/{userId}", userId)
    	.then()
    	.assertThat()
    	.statusCode(403);

    	given()
    	.spec(reqSpec)
    	.when()
    	.delete("api/users/v3/{userId}", userId)
    	.then()
    	.assertThat()
    	.statusCode(403);
    	
    	logger.info("test success, when a request was made without auth token the response was 401 UNAUTHORIZED "
    			  + "and when there was a token without ADMIN privileges then the response was 403 FORBIDDEN");
    
    }
    
}
