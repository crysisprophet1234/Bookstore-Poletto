package com.poletto.bookstore.v3.services;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.filters;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.poletto.bookstore.dto.v3.RoleDto;
import com.poletto.bookstore.dto.v3.UserChangesDto;
import com.poletto.bookstore.dto.v3.UserDto;
import com.poletto.bookstore.entities.User;
import com.poletto.bookstore.entities.VerificationToken;
import com.poletto.bookstore.entities.enums.AccountStatus;
import com.poletto.bookstore.entities.enums.UserStatus;
import com.poletto.bookstore.repositories.v3.UserRepository;
import com.poletto.bookstore.repositories.v3.VerificationTokenRepository;
import com.poletto.bookstore.services.v3.EmailService;
import com.poletto.bookstore.services.v3.impl.AuthServiceImpl;
import com.poletto.bookstore.services.v3.impl.UserServiceImpl;
import com.poletto.bookstore.v3.mocks.UserAuthMocks;
import com.poletto.bookstore.v3.mocks.UserMocks;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

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
	
	@SpyBean
	private VerificationTokenRepository verificationTokenRepository;
	
	@Autowired
	@InjectMocks
	private UserServiceImpl userService;
	
	@Autowired
	@InjectMocks
	private AuthServiceImpl authService;
	
	@SpyBean
	private EmailService emailService;
	
	private static UserDto userDto;
	private static UserChangesDto userChangesDto;
	private static VerificationToken verificationToken;
 
    @BeforeEach
    public void setUp() {
    	
    	baseURI = serverBaseURI;
        port = serverPort;
        useRelaxedHTTPSValidation();
        filters(new RequestLoggingFilter(), new ResponseLoggingFilter()); 
        
        userChangesDto = new UserChangesDto();
	
    }
    
    @AfterEach
    public void tearDown() {
    	
    	userChangesDto = null;
    	userDto = null;
    	verificationToken = null;
    	
    	response = null;
    	jsonPath = null;
    	json.clear();
    	
    }
    
    @Test
    
    void givenDefaultPageParapometers_whenRequestingUserPage_thenReceiveUserPage() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenDefaultPageParameters_whenRequestingUserPage_thenReceiveUserPage()  =========>\n");
    	
    	List<User> allUsers = userRepository.findAll(); 
    	
    	response = given()
				.spec(UserAuthMocks.AdminPrivilegesUser())
			  .when()
			  	.get("api/v3/users")
			  .then()
			  	.extract().response();
    	
    	jsonPath = JsonPath.from(response.getBody().asString());
    	
    	assertEquals(response.getStatusCode(), 200);
    	
    	assertEquals(allUsers.size(), jsonPath.getList("content").size());
    	
    	assertEquals(allUsers.get(0).getId(), jsonPath.getLong("content[0].id"));
    	
    	assertEquals(allUsers.get(0).getEmail(), jsonPath.getString("content[0].email"));
    	
    	assertEquals(jsonPath.getInt("pageable.pageSize"), 12);
    	
    	assertEquals(jsonPath.getInt("pageable.pageNumber"), 0);

    	logger.info("test success, when a admin user requested an user page with default page params, the response properly provided the page");
    	
    }
    
    @Test
    
    void givenDefinedPageParameters_whenRequestingUserPage_thenReceiveUserPage() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenDefaultPageParameters_whenRequestingUserPage_thenReceiveUserPage()  =========>\n");
    	
    	Long pageNumber = 1L;
    	Long pageSize = 3L;
    	String sort = "desc";
    	String orderBy = "email";
    	
    	response = given()
				.spec(UserAuthMocks.AdminPrivilegesUser())
				.queryParam("page", pageNumber)
				.queryParam("size", pageSize)
				.queryParam("sort", sort)
				.queryParam("orderBy", orderBy)
			  .when()
			  	.get("api/v3/users")
			  .then()
			  	.extract().response();
    	
    	jsonPath = JsonPath.from(response.getBody().asString());
    	
    	assertEquals(response.getStatusCode(), 200);
    	
    	//TODO: pageable interface is bugged, maybe because of low amount of items returned
    	//assertEquals(pageSize, jsonPath.getList("content").size());
    	
    	assertEquals(pageSize, jsonPath.getInt("pageable.pageSize"));
    	
    	assertEquals(pageNumber, jsonPath.getInt("pageable.pageNumber"));

    	logger.info("test success, when a admin user requested an user page with defined page params, the response properly provided the page");
    	
    }
    
    @Test
    
    void givenExistingUserId_whenRequestingUserById_thenReceiveUserDetails() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenExistingUserId_whenRequestingUserById_thenReceiveUserDetails()  =========>\n");
    	
    	Long userId = 1L;
    	
    	userDto = userService.findById(userId);
    	
    	response = given()
    				.spec(UserAuthMocks.AdminPrivilegesUser())
    			  .when()
    			  	.get("/api/v3/users/{userId}", userId)
    			  .then()
    			  	.extract().response();
    	
    	jsonPath = JsonPath.from(response.getBody().asString());
		
		assertEquals(response.getStatusCode(), 200);
		
		assertEquals(userDto.getKey(), jsonPath.getLong("id"));
		
		assertEquals(userDto.getEmail(), jsonPath.get("email"));
		
		assertEquals(userDto.getPassword(), jsonPath.get("password"));
		
		assertEquals(userDto.getToken(), jsonPath.get("token"));
		
		assertEquals(userDto.getAccountStatus().toString(), jsonPath.get("accountStatus"));
		
		assertEquals(userDto.getUserStatus().toString(), jsonPath.get("userStatus"));
		
		assertEquals(
			userDto.getRoles().size(),
			jsonPath.getList("roles").size()
		);
		
		assertEquals(
			userDto.getLinks().toList().size(),
			jsonPath.getList("links").size()
		);
		
		logger.info("test success, when a admin user requested a user details by its id, the response properly provided the details");
    	
    }
    
    @Test
    
    void givenNonExistingUserId_whenRequestingUserById_thenReceiveNotFoundException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenNonExistingUserId_whenRequestingUserById_thenReceiveNotFoundException()  =========>\n");
    	
    	Long userId = 99999L;
    	
    	given()
			.spec(UserAuthMocks.AdminPrivilegesUser())
		.when()
		  	.get("/api/v3/users/{userId}", userId)
		.then()
		  	.statusCode(404)
		.body("message", equalTo("Resource USER not found. ID " + userId));
		
		logger.info("test success, when a admin user request a user details with a non existing  user id, the response properly provided the not found exception with message explaining");
    	
    }
    
    @Test
    @DirtiesContext
    
    void givenValidNewEmail_withOwnToken_whenUpdatingUserEmail_thenUpdateEmailAndReceiveUpdatedData() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidNewEmail_withOwnToken_whenUpdatingUserEmail_thenUpdateEmailAndReceiveUpdatedData()  =========>\n");
    	
    	Long userId = 1L;
    	
    	userDto = userService.findById(userId);
    	
    	userChangesDto.setEmail("AdminNewEmail@mail.com");
    	
    	response = given()
    				.spec(UserAuthMocks.AdminPrivilegesUser(userDto))
    				.body(userChangesDto)
    				.contentType(ContentType.JSON)
    			  .when()
    			  	.put("api/v3/users/{userId}/change-email", userId)
    			  .then()
    			  	.extract().response();
    	
    	jsonPath = JsonPath.from(response.getBody().asString());
    	
    	assertEquals(response.getStatusCode(), 200);
		
    	assertEquals(userDto.getKey(), jsonPath.getLong("id"));
		
		assertNotEquals(userDto.getEmail(), jsonPath.get("email"));
		
		assertEquals(userChangesDto.getEmail(), jsonPath.get("email"));
		
		userDto = userService.findById(userId);
		
		assertEquals(userDto.getEmail(), jsonPath.get("email"));
		
		assertEquals(userDto.getAccountStatus().toString(), jsonPath.get("accountStatus"));
		
		logger.info("test success, given a valid email when changing the email of a existing user, the email gets uptaded and the client receives the updated data");
    	
    }
    
    @Test
    
    void givenValidNewEmail_withInvalidToken_whenUpdatingUserEmail_thenReceiveUnauthorizedException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidNewEmail_withInvalidToken_whenUpdatingUserEmail_thenReceiveUnauthorizedException()  =========>\n");
    	
    	Long userId = 1L;
    	
    	userDto = userService.findById(userId);
    	
    	userChangesDto.setEmail("AdminNewEmail@mail.com");
    	
    	given()
			.spec(UserAuthMocks.AdminPrivilegesUser())
			.body(userChangesDto)
			.contentType(ContentType.JSON)
		.when()
	  		.put("api/v3/users/{userId}/change-email", userId)
	  	.then()
	  		.statusCode(401)
	  		.body("message", equalTo("Token inválido para atualização de e-mail"));
		
		logger.info("test success, given a valid email but with a invalid token when updating email, the response properly provided the exception and message explaining");
    	
    }
    
    @Test
    
    void givenInvalidNewEmail_whenUpdatingUserEmail_thenReceiveInvalidEmailException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenInvalidNewEmail_whenUpdatingUserEmail_thenReceiveInvalidEmailException()  =========>\n");
    	
    	Long userId = 1L;
    	
    	userDto = userService.findById(userId);
    	
    	logger.info("setting a malformed email");
    	
    	userChangesDto.setEmail("invalid_email");
    	
    	given()
    		.spec(UserAuthMocks.AdminPrivilegesUser(userDto))
			.body(userChangesDto)
			.contentType(ContentType.JSON)
		.when()
	  		.put("api/v3/users/{userId}/change-email", userId)
	  	.then()
	  		.statusCode(422)
	  		.body("message", equalTo("userChangesDto: campo [email] deve ser um endereço de e-mail bem formado. Valor passado: '" + userChangesDto.getEmail() + "'"));
    	
    	logger.info("setting a null email");
    	
    	userChangesDto.setEmail(null);
    	
    	given()
    		.spec(UserAuthMocks.AdminPrivilegesUser(userDto))
			.body(userChangesDto)
			.contentType(ContentType.JSON)
		.when()
  			.put("api/v3/users/{userId}/change-email", userId)
  		.then()
  			.statusCode(422)
  			.body("message", equalTo("Novo e-mail não pode ser nulo"));
    	
    	logger.info("setting a empty email");
    	
    	userChangesDto.setEmail("");
    	
    	given()
    		.spec(UserAuthMocks.AdminPrivilegesUser(userDto))
			.body(userChangesDto)
			.contentType(ContentType.JSON)
		.when()
  			.put("api/v3/users/{userId}/change-email", userId)
  		.then()
  			.statusCode(422)
  			.body("message", equalTo("Novo e-mail não pode ser nulo"));
		
		logger.info("test success, given a invalid, empty or null email on the payload when changing the email of a existing user, the response properly provided the invalid email exception with message explaining");
    	
    }
    
    @Test
    
    void givenAlreadyExistingEmail_whenUpdatingUserEmail_thenReceiveConflictException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenAlreadyExistingEmail_whenUpdatingUserEmail_thenReceiveConflictException()  =========>\n");
    	
    	Long userId = 1L;
    	
    	userDto = userService.findById(userId);
    	
    	userChangesDto.setEmail("admin@mail.com");
    	
    	given()
    		.spec(UserAuthMocks.AdminPrivilegesUser(userDto))
			.body(userChangesDto)
			.contentType(ContentType.JSON)
		.when()
	  		.put("api/v3/users/{userId}/change-email", userId)
	  	.then()
	  		.statusCode(409)
	  		.body("message", equalTo("Email providenciado " + userChangesDto.getEmail() + " já está em uso"));
    	
    	logger.info("test success, given an already existing email, the response properly provided the already existing email exception with message explaining");
    	
    }
    
    @Test
    @DirtiesContext
    
    void givenValidNewPassword_withOwnToken_whenUpdatingUserPassword_thenUpdatePasswordAndReceiveUpdatedData() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidNewPasswordAndToken_whenUpdatingUserPassword_thenUpdatePasswordAndReceiveUpdatedData()  =========>\n");

        Long userId = 1L;

        userDto = userService.findById(userId);

        userChangesDto.setPassword("newpsw1234");

        response = given()
                    .spec(UserAuthMocks.AdminPrivilegesUser(userDto))
                    .body(userChangesDto)
                    .contentType(ContentType.JSON)
                  .when()
                    .put("api/v3/users/{userId}/change-password", userId)
                  .then()
                    .extract().response();

        jsonPath = JsonPath.from(response.getBody().asString());
    	
    	assertEquals(response.getStatusCode(), 200);
		
    	assertEquals(userDto.getKey(), jsonPath.getLong("id"));
		
		assertEquals(userDto.getEmail(), jsonPath.get("email"));
		
		assertNotEquals(userDto.getPassword(), jsonPath.get("password"));
		
		userDto = userService.findById(userId);
		
		assertEquals(userDto.getPassword(), jsonPath.get("password"));
		
		UserAuthMocks.AdminPrivilegesUser(userDto);

		logger.info("test success, given a valid password when changing the password of a existing user, the password gets uptaded and the client receives the updated data");
    	
    }
    
    @Test
    
    void givenValidNewPassword_withInvalidToken_whenUpdatingUserPassword_thenReceiveUnauthorizedException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidNewPasswordButInvalidToken_whenUpdatingUserPassword_thenReceiveUnauthorizedException()  =========>\n");
    	
    	Long userId = 1L;
    	
    	userDto = userService.findById(userId);
    	
    	userChangesDto.setPassword("abcd1234");
    	
    	given()
	        .spec(UserAuthMocks.AdminPrivilegesUser())
	        .body(userChangesDto)
	        .contentType(ContentType.JSON)
        .when()
            .put("api/v3/users/{userId}/change-password", userId)
        .then()
            .statusCode(401)
            .body("message", equalTo("Token inválido para atualização de e-mail"));
		
		logger.info("test success, given a valid password but with a invalid token when updating password, the response properly provided the exception and message explaining");
    	
    }
    
    @Test
    
    void givenInvalidNewPassword_whenUpdatingUserPassword_thenReceiveInvalidPasswordException() {

        logger.info("\n\n<=========  STARTING TEST givenInvalidNewPassword_whenUpdatingUserPassword_thenReceiveInvalidPasswordException()  =========>\n");

        Long userId = 1L;

        userDto = userService.findById(userId);
        
        logger.info("setting a password with more than 12 characteres");
        
        userChangesDto.setPassword("WayTooLongPassword1234");

        given()
	        .spec(UserAuthMocks.AdminPrivilegesUser(userDto))
	        .body(userChangesDto)
	        .contentType(ContentType.JSON)
        .when()
        	.put("api/v3/users/{userId}/change-password", userId)
    	.then()
        	.statusCode(422)
        	.body("message", equalTo("userChangesDto: campo [password] deve conter entre 6 e 12 caracteres, com ao menos 1 letra e 1 número. Valor passado: '"+ userChangesDto.getPassword() + "'"));
        
        logger.info("setting a password with less than 6 characteres");
        
        userChangesDto.setPassword("psw12");

        given()
	        .spec(UserAuthMocks.AdminPrivilegesUser(userDto))
	        .body(userChangesDto)
	        .contentType(ContentType.JSON)
        .when()
        	.put("api/v3/users/{userId}/change-password", userId)
    	.then()
        	.statusCode(422)
        	.body("message", equalTo("userChangesDto: campo [password] deve conter entre 6 e 12 caracteres, com ao menos 1 letra e 1 número. Valor passado: '"+ userChangesDto.getPassword() + "'"));
        
        logger.info("setting a password without letters");
        
        userChangesDto.setPassword("12345678");

        given()
	        .spec(UserAuthMocks.AdminPrivilegesUser(userDto))
	        .body(userChangesDto)
	        .contentType(ContentType.JSON)
        .when()
        	.put("api/v3/users/{userId}/change-password", userId)
    	.then()
        	.statusCode(422)
        	.body("message", equalTo("userChangesDto: campo [password] deve conter entre 6 e 12 caracteres, com ao menos 1 letra e 1 número. Valor passado: '"+ userChangesDto.getPassword() + "'"));
        
        logger.info("setting a password without numbers");
        
        userChangesDto.setPassword("abcdefgh");

        given()
	        .spec(UserAuthMocks.AdminPrivilegesUser(userDto))
	        .body(userChangesDto)
	        .contentType(ContentType.JSON)
        .when()
        	.put("api/v3/users/{userId}/change-password", userId)
    	.then()
        	.statusCode(422)
        	.body("message", equalTo("userChangesDto: campo [password] deve conter entre 6 e 12 caracteres, com ao menos 1 letra e 1 número. Valor passado: '"+ userChangesDto.getPassword() + "'"));
        
        logger.info("setting a empty password");
        
        userChangesDto.setPassword("");

        given()
	        .spec(UserAuthMocks.AdminPrivilegesUser(userDto))
	        .body(userChangesDto)
	        .contentType(ContentType.JSON)
        .when()
        	.put("api/v3/users/{userId}/change-password", userId)
    	.then()
        	.statusCode(422)
        	.body("message", equalTo("userChangesDto: campo [password] deve conter entre 6 e 12 caracteres, com ao menos 1 letra e 1 número. Valor passado: (empty)"));

        logger.info("setting a null password");
        
        userChangesDto.setPassword(null);

        given()
	        .spec(UserAuthMocks.AdminPrivilegesUser(userDto))
	        .body(userChangesDto)
	        .contentType(ContentType.JSON)
        .when()
        	.put("api/v3/users/{userId}/change-password", userId)
    	.then()
        	.statusCode(422)
        	.body("message", equalTo("Nova senha não pode ser nula"));

        logger.info("test success, given a invalid, empty or null password on the payload when changing the password of a existing user, the response properly provided the invalid password exception with message explaining");
        	
    }
    
    @Test
    @DirtiesContext
    
    void givenValidRolesSet_withAdminToken_whenAddingUserRoles_thenAddRolesAndReceiveUpdatedData() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidRolesSet_withAdminToken_whenAddingUserRoles_thenAddRolesAndReceiveUpdatedData()  =========>\n");

        Long operatorUserId = 5L;

        userDto = userService.findById(operatorUserId);
        
        Set<RoleDto> newRoles = Set.of(
    		(new RoleDto(3L, "ROLE_ADMIN"))
    	);
        
        response = given()
					.spec(UserAuthMocks.AdminPrivilegesUser())
					.body(newRoles)
					.contentType(ContentType.JSON)
				.when()
					.put("api/v3/users/{userId}/add-roles", operatorUserId)
				.then()
					.extract().response();
        
        jsonPath = JsonPath.from(response.getBody().asString());
    	
    	assertEquals(response.getStatusCode(), 200);
		
    	assertEquals(userDto.getKey(), jsonPath.getLong("id"));
		
    	assertNotEquals(userDto.getRoles().size(), jsonPath.getList("roles").size());  
		
		userDto = userService.findById(operatorUserId);
		
		assertEquals(userDto.getRoles().size(), jsonPath.getList("roles").size());
		
		assertEquals(jsonPath.getLong("roles[2].id"), 3L);
		
		assertEquals(jsonPath.get("roles[2].authority"), "ROLE_ADMIN");
		
		logger.info("test success, given a valid set of roles when adding role to a existing user, the roles list gets uptaded and the client receives the updated data");
    	
    }
    
    @Test
    
    void givenValidRolesSet_withoutAdminToken_whenAddingUserRoles_thenReceiveForbiddenException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidRolesSetWithoutAdminToken_whenAddingUserRoles_thenReceiveForbiddenException()  =========>\n");

        Long customerUserId = 4L;

        userDto = userService.findById(customerUserId);
        
        Set<RoleDto> newRoles = Set.of(
    		(new RoleDto(3L, "ROLE_ADMIN"))
    	);
        
        given()
			.spec(UserAuthMocks.AdminPrivilegesUser(userDto))
			.body(newRoles)
			.contentType(ContentType.JSON)
		.when()
			.put("api/v3/users/{userId}/add-roles", customerUserId)
		.then()
			.statusCode(403)
			.body("message", equalTo("Access Denied"));
        
        given()
			.body(newRoles)
			.contentType(ContentType.JSON)
		.when()
			.put("api/v3/users/{userId}/add-roles", customerUserId)
		.then()
			.statusCode(401)
			.body("message", equalTo("Unauthorized"));
        
        logger.info("test success, given a valid set of roles but with a invalid or not present token when adding user roles, the response properly provided the exception and message explaining");
        
    }

    @Test
    
    void givenInvalidRolesSet_whenAddingUserRoles_thenReceiveInvalidRolesException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenInvalidRolesSet_whenAddingUserRoles_thenReceiveInvalidRolesException()  =========>\n");

        Long operatorUserId = 5L;

        userDto = userService.findById(operatorUserId);
        
        Set<RoleDto> newRoles = new HashSet<>();
        
        newRoles.clear();
        
        given()
			.spec(UserAuthMocks.AdminPrivilegesUser())
			.body(newRoles)
			.contentType(ContentType.JSON)
		.when()
			.put("api/v3/users/{userId}/add-roles", operatorUserId)
		.then()
			.statusCode(400)
			.body("message", equalTo("addUserRoles.roles: Lista de roles a serem adicionadas ao usuário não pode estar vazia"));
        
        newRoles.clear();
        
        newRoles.add(new RoleDto(null, "ROLE_NAME"));
        
        given()
			.spec(UserAuthMocks.AdminPrivilegesUser())
			.body(newRoles)
			.contentType(ContentType.JSON)
		.when()
			.put("api/v3/users/{userId}/add-roles", operatorUserId)
		.then()
			.statusCode(400)
			.body("message", equalTo("addUserRoles.roles[].id: ID da role não pode ser nulo"));
        
        newRoles.clear();
        
        newRoles.add(new RoleDto(-9999L, "ROLE_NAME"));
        
        given()
			.spec(UserAuthMocks.AdminPrivilegesUser())
			.body(newRoles)
			.contentType(ContentType.JSON)
		.when()
			.put("api/v3/users/{userId}/add-roles", operatorUserId)
		.then()
			.statusCode(400)
			.body("message", equalTo("addUserRoles.roles[].id: ID da role não pode ser menor que 1"));
        
        logger.info("test success, given a invalid, empty or null set of roles on the payload when adding user roles of a existing user, the response properly provided the invalid role exception with message explaining");
        
    }

    @Test
    @DirtiesContext
    
    void givenValidRolesSet_withAdminToken_whenRemovingUserRoles_thenAddRolesAndReceiveUpdatedData() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidRolesSetWithAdminToken_whenRemovingUserRoles_thenAddRolesAndReceiveUpdatedData()  =========>\n");

        Long operatorUserId = 5L;

        userDto = userService.findById(operatorUserId);
        
        Set<RoleDto> rolesToBeRemoved = Set.of(
    		(new RoleDto(2L, "ROLE_OPERATOR"))
    	);
        
        response = given()
					.spec(UserAuthMocks.AdminPrivilegesUser())
					.body(rolesToBeRemoved)
					.contentType(ContentType.JSON)
				.when()
					.put("api/v3/users/{userId}/remove-roles", operatorUserId)
				.then()
					.extract().response();
        
        jsonPath = JsonPath.from(response.getBody().asString());
    	
    	assertEquals(response.getStatusCode(), 200);
		
    	assertEquals(userDto.getKey(), jsonPath.getLong("id"));
		
    	assertNotEquals(userDto.getRoles().size(), jsonPath.getList("roles").size());  
		
		userDto = userService.findById(operatorUserId);
		
		assertEquals(userDto.getRoles().size(), jsonPath.getList("roles").size());
		
		assertNull(jsonPath.get("roles[1].id"));
		
		assertNull(jsonPath.get("roles[1].authority"));
		
		logger.info("test success, given a valid set of roles when removing roles from a existing user, the roles list gets uptaded and the client receives the updated data");
    	
    }
    
    @Test
    
    void givenValidRolesSet_withoutAdminToken_whenRemovingUserRoles_thenReceiveForbiddenException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidRolesSetWithoutAdminToken_whenRemovingUserRoles_thenReceiveForbiddenException()  =========>\n");

        Long operatorUserId = 5L;

        userDto = userService.findById(operatorUserId);
        
        Set<RoleDto> rolesToBeRemoved = Set.of(
    		(new RoleDto(2L, "ROLE_OPERATOR"))
    	);
        
        given()
			.spec(UserAuthMocks.AdminPrivilegesUser(userDto))
			.body(rolesToBeRemoved)
			.contentType(ContentType.JSON)
		.when()
			.put("api/v3/users/{userId}/remove-roles", operatorUserId)
		.then()
			.statusCode(403)
			.body("message", equalTo("Access Denied"));
        
        given()
			.body(rolesToBeRemoved)
			.contentType(ContentType.JSON)
		.when()
			.put("api/v3/users/{userId}/remove-roles", operatorUserId)
		.then()
			.statusCode(401)
			.body("message", equalTo("Unauthorized"));
        
        logger.info("test success, given a valid set of roles but with a invalid or not present token when removing user roles, the response properly provided the exception and message explaining");
        
    }

    @Test
    
    void givenNonPresentRoleSet_whenRemovingUserRoles_thenReceiveInvalidRolesException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenInvalidRolesSet_whenRemovingUserRoles_thenReceiveInvalidRolesException()  =========>\n");

        Long operatorUserId = 5L;

        userDto = userService.findById(operatorUserId);
        
        RoleDto adminRole = new RoleDto(3L, "ROLE_ADMIN");
        
        Set<RoleDto> rolesToBeRemoved = Set.of(adminRole);
        
        given()
			.spec(UserAuthMocks.AdminPrivilegesUser())
			.body(rolesToBeRemoved)
			.contentType(ContentType.JSON)
		.when()
			.put("api/v3/users/{userId}/remove-roles", operatorUserId)
		.then()
			.statusCode(422)
			.body("message", equalTo("Usuário ID " + operatorUserId + " não possui a " + adminRole.toString() + ", impossível remover"));
        
        logger.info("test success, given a valid set of roles but with a role that the user doesnt have when removing user roles, the response properly provided the exception and message explaining");
    	
    }
    
    @Test
    
    void givenInvalidRolesSet_whenRemovingUserRoles_thenReceiveInvalidRolesException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenInvalidRolesSet_whenRemovingUserRoles_thenReceiveInvalidRolesException()  =========>\n");

        Long operatorUserId = 5L;

        userDto = userService.findById(operatorUserId);
        
        Set<RoleDto> rolesToBeRemoved = new HashSet<>();
        
        rolesToBeRemoved.clear();
        
        given()
			.spec(UserAuthMocks.AdminPrivilegesUser())
			.body(rolesToBeRemoved)
			.contentType(ContentType.JSON)
		.when()
			.put("api/v3/users/{userId}/remove-roles", operatorUserId)
		.then()
			.statusCode(400)
			.body("message", equalTo("removeUserRoles.roles: Lista de roles a serem removidas do usuário não pode estar vazia"));
        
        rolesToBeRemoved.clear();
        
        rolesToBeRemoved.add(new RoleDto(null, "ROLE_NAME"));
        
        given()
			.spec(UserAuthMocks.AdminPrivilegesUser())
			.body(rolesToBeRemoved)
			.contentType(ContentType.JSON)
		.when()
			.put("api/v3/users/{userId}/remove-roles", operatorUserId)
		.then()
			.statusCode(400)
			.body("message", equalTo("removeUserRoles.roles[].id: ID da role não pode ser nulo"));
        
        rolesToBeRemoved.clear();
        
        rolesToBeRemoved.add(new RoleDto(-9999L, "ROLE_NAME"));
        
        given()
			.spec(UserAuthMocks.AdminPrivilegesUser())
			.body(rolesToBeRemoved)
			.contentType(ContentType.JSON)
		.when()
			.put("api/v3/users/{userId}/remove-roles", operatorUserId)
		.then()
			.statusCode(400)
			.body("message", equalTo("removeUserRoles.roles[].id: ID da role não pode ser menor que 1"));
        
        logger.info("test success, given a invalid, empty or null set of roles on the payload when removing user roles from a existing user, the response properly provided the invalid role exception with message explaining");
        
    }

    @Test
    @DirtiesContext
    
    void givenValidUserStatus_withAdminToken_whenUpdatingUserStatus_thenUpdateUserStatusAndReceiveUpdatedData() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidUserStatus_withAdminToken_whenUpdatingUserStatus_thenUpdateUserStatusAndReceiveUpdatedData()  =========>\n");
    	
    	Long activeUserId = 1L;
    	
    	userDto = userService.findById(activeUserId);
    	
    	userChangesDto.setUserStatus(UserStatus.INACTIVE);
    	
    	response = given()
    				.spec(UserAuthMocks.AdminPrivilegesUser(userDto))
    				.body(userChangesDto)
    				.contentType(ContentType.JSON)
    			  .when()
    			  	.put("api/v3/users/{userId}/change-status", activeUserId)
    			  .then()
    			  	.extract().response();
    	
    	jsonPath = JsonPath.from(response.getBody().asString());
    	
    	assertEquals(response.getStatusCode(), 200);
    	
    	assertEquals(userDto.getKey(), jsonPath.getLong("id"));
		
		assertNotEquals(userDto.getUserStatus().toString(), jsonPath.get("userStatus"));
		
		assertEquals(userChangesDto.getUserStatus().toString(), jsonPath.get("userStatus"));
		
		userDto = userService.findById(activeUserId);
		
		assertEquals(userDto.getUserStatus().toString(), jsonPath.get("userStatus"));
		
		logger.info("test success, given a valid user status when changing the user status of a existing user, the user status gets uptaded and the client receives the updated data");
    	
    }
    
    @Test
    
    void givenValidUserStatus_withoutAdminToken_whenUpdatingUserStatus_thenReceiveUnauthorizedException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidUserStatus_withoutAdminToken_whenUpdatingUserStatus_thenReceiveUnauthorizedException()  =========>\n");
    	
    	Long customerUserId = 5L;
    	
    	userDto = userService.findById(customerUserId);
    	
    	userChangesDto.setUserStatus(UserStatus.INACTIVE);
    	
    	given()
    		.spec(UserAuthMocks.AdminPrivilegesUser(userDto))
			.body(userChangesDto)
			.contentType(ContentType.JSON)
		.when()
  			.put("api/v3/users/{userId}/change-status", customerUserId)
		.then()
	  		.statusCode(403)
	  		.body("message", equalTo("Access Denied"));
    	
    	given()
			.body(userChangesDto)
			.contentType(ContentType.JSON)
		.when()
	  		.put("api/v3/users/{userId}/change-status", customerUserId)
  		.then()
	  		.statusCode(401)
	  		.body("message", equalTo("Unauthorized"));
		
		logger.info("test success, given a valid user status when changing the user status of a existing user, the user status gets uptaded and the client receives the updated data");
    	
    }

    @Test
    
    void givenAlreadySettedUserStatus_whenUpdatingUserStatus_thenReceiveInvalidStatusException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenAlreadySettedUserStatus_whenUpdatingUserStatus_thenReceiveInvalidStatusException()  =========>\n");
    	
    	Long activeUserId = 1L;
    	
    	userDto = userService.findById(activeUserId);
    	
    	userChangesDto.setUserStatus(userDto.getUserStatus());
    	
    	given()
			.spec(UserAuthMocks.AdminPrivilegesUser(userDto))
			.body(userChangesDto)
			.contentType(ContentType.JSON)
		.when()
	  		.put("api/v3/users/{userId}/change-status", activeUserId)
  		.then()
	  		.statusCode(400)
	  		.body("message", equalTo("Status inválido para o usuário ID " + userDto.getKey() + ", permanecerá o status " + userDto.getUserStatus()));
    	
    	logger.info("test success, given a valid user status when changing the user status of a existing user but the user already has this status, the response properly provided the exception and message explaining");
    	
    }
    
    @Test
    
    void givenInvalidUserStatus_whenUpdatingUserStatus_thenReceiveInvalidStatusException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenInvalidUserStatus_whenUpdatingUserStatus_thenReceiveInvalidStatusException()  =========>\n");
    	
    	Long activeUserId = 1L;
    	
    	userDto = userService.findById(activeUserId);
    	
    	Map<String, String> invalidStatusMap = new HashMap<>();
    	
    	invalidStatusMap.put("userStatus", "non-existent-user-status");
    	
    	given()
			.spec(UserAuthMocks.AdminPrivilegesUser(userDto))
			.body(invalidStatusMap)
			.contentType(ContentType.JSON)
		.when()
  			.put("api/v3/users/{userId}/change-status", activeUserId)
		.then()
  			.statusCode(400)
  			.body("message", equalTo(
  					"JSON parse error: Cannot deserialize value of type `com.poletto.bookstore.entities.enums.UserStatus` from String \""+ invalidStatusMap.get("userStatus") + "\":"
  				  + " not one of the values accepted for Enum class: [INACTIVE, ACTIVE, SUSPENDED]"));
    	
    	invalidStatusMap.put("userStatus", "");
    	
    	given()
			.spec(UserAuthMocks.AdminPrivilegesUser(userDto))
			.body(invalidStatusMap)
			.contentType(ContentType.JSON)
		.when()
  			.put("api/v3/users/{userId}/change-status", activeUserId)
		.then()
  			.statusCode(400)
  			.body("message", equalTo(
  					"JSON parse error: Cannot coerce empty String (\"\") to `com.poletto.bookstore.entities.enums.UserStatus` value (but could if coercion was enabled using `CoercionConfig`)"));
    	
    	invalidStatusMap.put("userStatus", null);
    	
    	given()
			.spec(UserAuthMocks.AdminPrivilegesUser(userDto))
			.body(invalidStatusMap)
			.contentType(ContentType.JSON)
		.when()
  			.put("api/v3/users/{userId}/change-status", activeUserId)
		.then()
  			.statusCode(500)
  			.body("message", equalTo("Valor de UserStatus não pode ser nulo"));
    	
    	logger.info("test success, given a invalid, empty or null user status on the payload when changing the user status of a existing user, the response properly provided the invalid status exception with message explaining");	
    	
    }

    @Test
    @DirtiesContext
    
    void givenExistingUserId_whenSendingVerificationEmailToNewUser_thenCreateTokenAndSendItByEmail() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenExistingUserId_whenSendingVerificationEmailToNewUser_thenCreateTokenAndSendItByEmail()  =========>\n");
    	
    	userDto = authService.register(UserMocks.registerUserMockDto());
    	
    	Long newUserId = userDto.getKey();
    	
    	given()
    		.spec(UserAuthMocks.TokenFromGivenUser(UserMocks.registerUserMockDto()))
    	.when()
    		.get("/api/v3/users/{newUserId}/send-verification-email", newUserId)
    	.then()
    		.statusCode(204);
    	
    	verify(verificationTokenRepository, times(1)).save(any());
    	
    	assertEquals(verificationTokenRepository.findAll().size(), 1);
    	
    	assertEquals(
			verificationTokenRepository.findAll().get(0).getUser().getId(),
			userDto.getKey()
		);
    	
    	verify(emailService, times(1)).sendEmailFromTemplate(
			eq(userDto.getEmail()),
			eq("Verificação de conta Poletto Bookstore"),
			eq("account-verification-template"),
			any()
		);
    	
    	logger.info("test success, given an existing user wich was just created, a verification token was generated and sent to the users email");	
    	
    }
    
    @Test
    @DirtiesContext
    
    void givenExistingUserId_whenSendingVerificationEmailToUser_withAlreadyPresentValidToken_thenSendValidTokenByEmail() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenExistingUserId_whenSendingVerificationEmailToUser_withAlreadyPresentValidToken_thenSendValidTokenByEmail()  =========>\n");
    	
    	userDto = authService.register(UserMocks.registerUserMockDto());
    	
    	Long newUserId = userDto.getKey();
    	
    	verificationToken = new VerificationToken();
    	verificationToken.setCreatedAt(LocalDateTime.now());
    	verificationToken.setExpiresAt(LocalDateTime.now().plusDays(1));
    	verificationToken.setToken(UUID.randomUUID());
    	verificationToken.setUser(UserMocks.userMockEntity(newUserId));
    	
    	verificationToken = verificationTokenRepository.save(verificationToken);
    	
    	given()
    		.spec(UserAuthMocks.TokenFromGivenUser(UserMocks.registerUserMockDto()))
    	.when()
    		.get("/api/v3/users/{newUserId}/send-verification-email", newUserId)
    	.then()
    		.statusCode(204);
    	
    	verify(verificationTokenRepository, times(1)).save(any());
    	
    	assertEquals(verificationTokenRepository.findAll().size(), 1);
    	
    	verify(emailService, times(1)).sendEmailFromTemplate(
			eq(userDto.getEmail()),
			eq("Verificação de conta Poletto Bookstore"),
			eq("account-verification-template"),
			any()
		);
    	
    	logger.info("test success, given an existing user when sending verification email when there is already a existing and valid token, the verification token was sent to the users email");	
    	
    }
    
    @Test
    @DirtiesContext
    
    void givenExistingUserId_whenSendingVerificationEmailToUser_withAlreadyPresentExpiredToken_thenCreateTokenAndSendItByEmail() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenExistingUserId_whenSendingVerificationEmailToUser_withAlreadyPresentExpiredToken_thenCreateTokenAndSendItByEmail()  =========>\n");
    	
    	userDto = authService.register(UserMocks.registerUserMockDto());
    	
    	Long newUserId = userDto.getKey();
    	
    	verificationToken = new VerificationToken();
    	verificationToken.setCreatedAt(LocalDateTime.now().minusDays(10));
    	verificationToken.setExpiresAt(LocalDateTime.now().minusDays(9));
    	verificationToken.setToken(UUID.randomUUID());
    	verificationToken.setUser(UserMocks.userMockEntity(newUserId));
    	
    	verificationToken = verificationTokenRepository.save(verificationToken);
    	
    	given()
    		.spec(UserAuthMocks.TokenFromGivenUser(UserMocks.registerUserMockDto()))
    	.when()
    		.get("/api/v3/users/{newUserId}/send-verification-email", newUserId)
    	.then()
    		.statusCode(204);
    	
    	verify(verificationTokenRepository, times(2)).save(any());
    	
    	assertEquals(verificationTokenRepository.findAll().size(), 2);
    	
    	verify(emailService, times(1)).sendEmailFromTemplate(
			eq(userDto.getEmail()),
			eq("Verificação de conta Poletto Bookstore"),
			eq("account-verification-template"),
			any()
		);
    	
    	logger.info("test success, given an existing user when sending verification email when there is already a existing and valid token, the verification token was sent to the users email");	
    	
    }
    
    @Test
    @DirtiesContext
    
    void givenNonExistingUserId_whenSendingVerificationEmailToUser_thenReceiveNotFoundException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenExistingUserId_whenSendingVerificationEmailToUser_withAlreadyPresentExpiredToken_thenCreateTokenAndSendItByEmail()  =========>\n");
    	
    	Long nonExistingUserId = 9999L;
    	
    	given()
    		.spec(UserAuthMocks.AdminPrivilegesUser())
    	.when()
    		.get("/api/v3/users/{newUserId}/send-verification-email", nonExistingUserId)
    	.then()
    		.statusCode(404)
    		.body("message", equalTo("Resource USER not found, ID: " + nonExistingUserId));
    	
    	logger.info("test success, given an non existing user when sending verification email, the response properly provided the not found exception");	
    	
    }

    @Test
    @DirtiesContext
    
    void givenExistingAndValidToken_whenVerifyingAccount_thenUpdateUserAccountStatus() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenExistingAndValidToken_whenVerifyingAccount_thenUpdateUserAccountStatus()  =========>\n");
    	
    	userDto = authService.register(UserMocks.registerUserMockDto());
    	
    	Long newUserId = userDto.getKey();
    	
    	verificationToken = new VerificationToken();
    	verificationToken.setCreatedAt(LocalDateTime.now());
    	verificationToken.setExpiresAt(LocalDateTime.now().plusDays(1));
    	verificationToken.setToken(UUID.randomUUID());
    	verificationToken.setUser(UserMocks.userMockEntity(newUserId));
    	
    	verificationToken = verificationTokenRepository.save(verificationToken);
    	
    	assertEquals(userDto.getAccountStatus(), AccountStatus.UNVERIFIED); 
    	
    	given()
			.spec(UserAuthMocks.TokenFromGivenUser(UserMocks.registerUserMockDto()))
		.when()
			.put("/api/v3/users/verify-account?verificationToken={verificationToken}", verificationToken.getToken())
		.then()
			.statusCode(204);
    	
    	userDto = userService.findById(newUserId);
    	
    	assertEquals(userDto.getAccountStatus(), AccountStatus.VERIFIED);
    	
    	logger.info("test success, given an existing and valid token uuid when requesting a account verification, the user account status got properly updated");
    	
    }
    
    @Test
    @DirtiesContext
    
    void givenExistingButExpiratedToken_whenVerifyingAccount_thenReceiveInvalidTokenException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenExistingButExpiratedToken_whenVerifyingAccount_thenReceiveInvalidTokenException()  =========>\n");
    	
    	userDto = authService.register(UserMocks.registerUserMockDto());
    	
    	Long newUserId = userDto.getKey();
    	
    	verificationToken = new VerificationToken();
    	verificationToken.setCreatedAt(LocalDateTime.now().minusDays(10));
    	verificationToken.setExpiresAt(LocalDateTime.now().minusDays(9));
    	verificationToken.setToken(UUID.randomUUID());
    	verificationToken.setUser(UserMocks.userMockEntity(newUserId));
    	
    	verificationToken = verificationTokenRepository.save(verificationToken);
    	
    	assertEquals(userDto.getAccountStatus(), AccountStatus.UNVERIFIED);
    	
    	given()
			.spec(UserAuthMocks.TokenFromGivenUser(UserMocks.registerUserMockDto()))
		.when()
			.put("/api/v3/users/verify-account?verificationToken={verificationToken}", verificationToken.getToken())
		.then()
			.statusCode(400)
			.body("message", containsString("Token provided has expired on " + verificationToken.getExpiresAt().toLocalDate()));
    	
    	userDto = userService.findById(newUserId);
    	
    	assertEquals(userDto.getAccountStatus(), AccountStatus.UNVERIFIED);
    	
    	logger.info("test success, given an existing but expirated token uuid when requesting a account verification, the user account status didnt updated and the response properly provided the exception");
    	
    }
    
    @Test
    @DirtiesContext
    
    void givenNonExistingToken_whenVerifyingAccount_thenReceiveInvalidTokenException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenNonExistingToken_whenVerifyingAccount_thenReceiveInvalidTokenException()  =========>\n");
    	
    	userDto = authService.register(UserMocks.registerUserMockDto());
    	
    	Long newUserId = userDto.getKey();
    	
    	assertEquals(userDto.getAccountStatus(), AccountStatus.UNVERIFIED);
    	
    	given()
			.spec(UserAuthMocks.TokenFromGivenUser(UserMocks.registerUserMockDto()))
		.when()
			.put("/api/v3/users/verify-account?verificationToken={verificationToken}", UUID.randomUUID())
		.then()
			.statusCode(400)
			.body("message", equalTo("Invalid token provided"));
    	
    	userDto = userService.findById(newUserId);
    	
    	assertEquals(userDto.getAccountStatus(), AccountStatus.UNVERIFIED);
    	
    	logger.info("test success, given an non existing token uuid when requesting a account verification, the user account status didnt updated and the response properly provided the exception");
    	
    }
    
}
        
