package com.poletto.bookstore.v3.services;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.filters;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.hateoas.Link;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.poletto.bookstore.dto.v3.AddressDto;
import com.poletto.bookstore.dto.v3.PersonDto;
import com.poletto.bookstore.dto.v3.UserDto;
import com.poletto.bookstore.repositories.v3.PersonRepository;
import com.poletto.bookstore.services.v3.impl.AuthServiceImpl;
import com.poletto.bookstore.services.v3.impl.PersonServiceImpl;
import com.poletto.bookstore.services.v3.impl.UserServiceImpl;
import com.poletto.bookstore.v3.mocks.PersonMocks;
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
public class PersonServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(PersonServiceTest.class);
	
	@LocalServerPort
	private int serverPort;
	
	private final String serverBaseURI = "https://localhost";
	
	private Response response;
	private JsonPath jsonPath;
	private Map<String, String> json = new HashMap<>();
	
	@SpyBean
	private PersonRepository personRepository;
	
	@Autowired
	@InjectMocks
	private PersonServiceImpl personService;
	
	@Autowired
	@InjectMocks
	private UserServiceImpl userService;
	
	@Autowired
	@InjectMocks
	private AuthServiceImpl authService;
	
	private static PersonDto personDto;
	
	private static UserDto userDto;
	
	private static AddressDto addressDto;
	
	@BeforeEach
    public void setUp() {
    	
    	baseURI = serverBaseURI;
        port = serverPort;
        useRelaxedHTTPSValidation();
        filters(new RequestLoggingFilter(), new ResponseLoggingFilter()); 
	
    }
    
    @AfterEach
    public void tearDown() {
    	
    	personDto = null;
    	userDto = null;
    	addressDto = null;
    	
    	response = null;
    	jsonPath = null;
    	json.clear();
    	
    }
    
    @Test
    void givenExistingPersonId_whenRequestingPersonById_thenReceivePersonDetails() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenExistingPersonId_whenRequestingPersonById_thenReceivePersonDetails()  =========>\n");
    	
    	long personId = 5L;
    	
    	personDto = personService.findById(personId);
    	
    	response = given()
				.spec(UserAuthMocks.CustomerPrivilegesUser())
			  .when()
			  	.get("/api/v3/persons/{personId}", personId)
			  .then()
			  	.extract().response();
    	
    	jsonPath = JsonPath.from(response.getBody().asString());
		
		assertEquals(200, response.getStatusCode());
		
		assertEquals(personDto.getKey(), jsonPath.getLong("id"));
		
		assertEquals(personDto.getFirstName(), jsonPath.getString("firstName"));
		
		assertEquals(personDto.getLastName(), jsonPath.getString("lastName"));
		
		assertEquals(personDto.getDateOfBirth().toString(), jsonPath.getString("dateOfBirth"));
		
		assertEquals(personDto.getGender(), jsonPath.getString("gender"));
		
		assertEquals(personDto.getPhone(), jsonPath.getString("phone"));
		
		assertEquals(personDto.getNationality(), jsonPath.getString("nationality"));
		
		assertEquals(personDto.getProfilePictureUrl(), jsonPath.getString("profilePictureUrl"));
		
		assertEquals(personDto.getCpf(), jsonPath.getString("cpf"));
		
		assertEquals(personDto.getUser().getKey(), jsonPath.getLong("user.id"));
		
		assertEquals(personDto.getUser().getEmail(), jsonPath.getString("user.email"));
		
		assertEquals(personDto.getUser().getUserStatus().toString(), jsonPath.getString("user.userStatus"));
		
		List<AddressDto> addressDtos = personDto.getAddresses();
		
		for (int i = 0; i < addressDtos.size(); i++) {
		    String addressPath = "addresses[" + i + "]";
		    AddressDto addressDto = addressDtos.get(i);

		    assertEquals(addressDto.getId(), jsonPath.getLong(addressPath + ".id"));
		    assertEquals(addressDto.getCep(), jsonPath.getString(addressPath + ".cep"));
		    assertEquals(addressDto.getAddressName(), jsonPath.getString(addressPath + ".addressName"));
		    assertEquals(addressDto.getNumber(), jsonPath.getInt(addressPath + ".number"));
		    assertEquals(addressDto.getComplement(), jsonPath.getString(addressPath + ".complement"));
		    assertEquals(addressDto.getDistrict(), jsonPath.getString(addressPath + ".district"));
		    assertEquals(addressDto.getCity(), jsonPath.getString(addressPath + ".city"));
		    assertEquals(addressDto.getState(), jsonPath.getString(addressPath + ".state"));
		}

		List<Link> linkDtos = personDto.getLinks().toList();
		
		for (int i = 0; i < linkDtos.size(); i++) {
		    String linkPath = "links[" + i + "]";
		    Link link = linkDtos.get(i);

		    assertEquals(link.getRel().toString(), jsonPath.getString(linkPath + ".rel"));
		    assertEquals(link.getType().toString(), jsonPath.getString(linkPath + ".type"));
		    assertThat((jsonPath.getString(linkPath + ".href")).contains(link.getHref().toString()));
		}
		
		logger.info("test success, when the user requested a person details by its id, the response properly provided the details");
    	
    }
    
    @Test
    void givenNonExistingPersonId_whenRequestingPersonById_thenReceiveNotFoundException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenNonExistingPersonId_whenRequestingPersonById_thenReceiveNotFoundException()  =========>\n");
    	
    	Long personId = 99999L;
    	
    	given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
		.when()
		  	.get("/api/v3/persons/{personId}", personId)
		.then()
		  	.statusCode(404)
		.body("message", equalTo("Resource PERSON not found. ID " + personId));
		
		logger.info("test success, when the user requested a person details with a non existing person id, the response properly provided the not found exception with message explaining");
    	
    }
    
    @Test
    void givenAnyPersonId_whenRequestingPersonById_withoutToken_thenReceiveForbiddenException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenAnyPersonId_whenRequestingPersonById_withoutToken_thenReceiveForbiddenException()  =========>\n");
    	
    	Long personId = 1L;
    	
    	given()
		.when()
  			.get("api/v3/persons/{personId}", personId)
  		.then()
	  		.statusCode(401)
	  		.body("message", equalTo("Unauthorized"));
    	
    	logger.info("test success, when requesting person by id without an token, the response properly provided the exception and message explaining");
    	
    }
    
    @Test
    @DirtiesContext
    void givenValidData_whenCreatingPerson_thenCreatePersonAndReturnPersonDetails() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidData_whenCreatingPerson_thenCreatePersonAndReturnPersonDetails()  =========>\n");
    	
    	userDto = authService.register(UserMocks.registerUserMockDto());
    	
    	Long newUserId = userDto.getKey();
    	
    	Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("firstName", "John");
        dataMap.put("lastName", "Doe");
        dataMap.put("dateOfBirth", "1990-05-15");
        dataMap.put("gender", "Male");
        dataMap.put("phone", "99876543210");
        dataMap.put("cpf", "12345678901");
        dataMap.put("user", Map.of("id", newUserId));
    	
    	response = given()
					.spec(UserAuthMocks.CustomerPrivilegesUser())
					.body(dataMap)
					.contentType(ContentType.JSON)
				.when()
				  	.post("/api/v3/persons/create")
				.then()
					.statusCode(201)
				.and()
					.extract()
					.response();
    	
    	jsonPath = JsonPath.from(response.getBody().asString());
    	
    	assertEquals(dataMap.get("firstName"), jsonPath.get("firstName"));
    	
        assertEquals(dataMap.get("lastName"), jsonPath.get("lastName"));
        
        assertEquals(dataMap.get("dateOfBirth"), jsonPath.get("dateOfBirth"));
        
        assertEquals(dataMap.get("gender"), jsonPath.get("gender"));
        
        assertEquals(dataMap.get("phone"), jsonPath.get("phone"));
        
        assertEquals(dataMap.get("cpf"), jsonPath.get("cpf"));
        
        assertEquals(newUserId, jsonPath.getLong("user.id"));
    
        logger.info("test success, when creating a  person with valid data and existing user id, the person was created and the response provided the new person details");
    		
    }
    
    @Test
    @DirtiesContext
    void givenValidData_whenCreatingPerson_withoutToken_thenReceiveForbiddenException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidData_whenCreatingPerson_withoutToken_thenReceiveForbiddenException()  =========>\n");
    	
    	Long newUserId = 9999L;
    	
    	Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("firstName", "John");
        dataMap.put("lastName", "Doe");
        dataMap.put("dateOfBirth", "1990-05-15");
        dataMap.put("gender", "Male");
        dataMap.put("phone", "99876543210");
        dataMap.put("cpf", "12345678901");
        dataMap.put("user", Map.of("id", newUserId));
    	
    	given()
			.body(dataMap)
			.contentType(ContentType.JSON)
		.when()
	  		.post("/api/v3/persons/create")
	  	.then()
		  	.statusCode(401)
	  		.body("message", equalTo("Unauthorized"));
    	
    	logger.info("test success, when trying to create new person without an token, the response properly provided the exception and message explaining");
    	
    }
    
    @Test
    @DirtiesContext
    void givenInvalidData_whenCreatingPerson_thenReceiveInvalidEntityException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidData_whenCreatingPerson_thenCreatePersonAndReturnPersonDetails()  =========>\n");
    	
    	userDto = authService.register(UserMocks.registerUserMockDto());
    	
    	Long newUserId = userDto.getKey();
    	
    	Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("firstName", "John");
        dataMap.put("lastName", "Doe");
        dataMap.put("dateOfBirth", "1990-05-15");
        dataMap.put("gender", "Male");
        dataMap.put("phone", "99876543210");
        dataMap.put("cpf", "12345678901");
        dataMap.put("user", Map.of("id", newUserId));
        
        Map<String, Object> invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("setting invalid firstname");
        
        invalidDataMap.put("firstName", "invalid firstname 333");
    	
    	given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidDataMap)
			.contentType(ContentType.JSON)
		.when()
		  	.post("/api/v3/persons/create")
		.then()
			.statusCode(422)
			.body("message", equalTo("personDto: campo [firstName] aceita apenas letras. Valor passado: '" + invalidDataMap.get("firstName") + "'"));
    	
    	invalidDataMap = new HashMap<>(dataMap);
    	
    	logger.info("setting invalid lastname");
        
    	invalidDataMap.put("lastName", "invalid lastname 333");
        
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidDataMap)
			.contentType(ContentType.JSON)
		.when()
	  		.post("/api/v3/persons/create")
	  	.then()
			.statusCode(422)
			.body("message", equalTo("personDto: campo [lastName] aceita apenas letras. Valor passado: '" + invalidDataMap.get("lastName") + "'"));
        
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("setting invalid date of birth");
        
        invalidDataMap.put("dateOfBirth", "3990-05-15");
        
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidDataMap)
			.contentType(ContentType.JSON)
		.when()
  			.post("/api/v3/persons/create")
  		.then()
			.statusCode(422)
			.body("message", equalTo("personDto: campo [dateOfBirth] não pode ser data atual ou futura. Valor passado: '" + invalidDataMap.get("dateOfBirth") + "'"));
        
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("setting invalid gender");
        
        invalidDataMap.put("gender", "invalid gender 333");
        
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidDataMap)
			.contentType(ContentType.JSON)
		.when()
  			.post("/api/v3/persons/create")
  		.then()
			.statusCode(422)
			.body("message", equalTo("personDto: campo [gender] aceita apenas letras. Valor passado: '" + invalidDataMap.get("gender") + "'"));
        
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("setting invalid phone");
        
        invalidDataMap.put("phone", "invalid num");
        
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidDataMap)
			.contentType(ContentType.JSON)
		.when()
  			.post("/api/v3/persons/create")
  		.then()
			.statusCode(422)
			.body("message", equalTo("personDto: campo [phone] aceita apenas números no formato 99877776666. Valor passado: '" + invalidDataMap.get("phone") + "'"));
        
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("setting invalid phone");
        
        invalidDataMap.put("phone", "99876543210111");
        
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidDataMap)
			.contentType(ContentType.JSON)
		.when()
  			.post("/api/v3/persons/create")
  		.then()
			.statusCode(422)
			.body("message", equalTo("personDto: campo [phone] aceita apenas números no formato 99877776666. Valor passado: '" + invalidDataMap.get("phone") + "'"));
        
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("setting invalid cpf");
        
        invalidDataMap.put("cpf", "invalid num");
        
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidDataMap)
			.contentType(ContentType.JSON)
		.when()
  			.post("/api/v3/persons/create")
  		.then()
			.statusCode(422)
			.body("message", equalTo("personDto: campo [cpf] aceita apenas números no formato 11122233344. Valor passado: '" + invalidDataMap.get("cpf") + "'"));
        
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("setting invalid cpf");
        
        invalidDataMap.put("cpf", "99876543210111");
        
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidDataMap)
			.contentType(ContentType.JSON)
		.when()
  			.post("/api/v3/persons/create")
  		.then()
			.statusCode(422)
			.body("message", equalTo("personDto: campo [cpf] aceita apenas números no formato 11122233344. Valor passado: '" + invalidDataMap.get("cpf") + "'"));
        
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("setting invalid user");
        
        invalidDataMap.put("user", null);
        
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidDataMap)
			.contentType(ContentType.JSON)
		.when()
  			.post("/api/v3/persons/create")
  		.then()
			.statusCode(422)
			.body("message", equalTo("personDto: campo [user] não deve ser nulo. Valor passado: (null)"));
        
        logger.info("test success, when trying to insert new person with invalid data, the response properly provided the exception with details");
    	
    }
    
    @Test
    @DirtiesContext
    void givenValidData_whenUpdatingPerson_thenUpdatePersonAndReturnUpdatedPersonDetails() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidData_whenUpdatingPerson_thenUpdatePersonAndReturnUpdatedPersonDetails()  =========>\n");
    	
    	userDto = authService.register(UserMocks.registerUserMockDto());
    	
    	Long newUserId = userDto.getKey();
    	
    	personDto = personService.create(newUserId, PersonMocks.insertPersonMock(userDto));
        
        personDto.setFirstName("Bob");
        personDto.setLastName("Lee");
        personDto.setGender("Female");
        personDto.setPhone("58862939237");
        personDto.setProfilePictureUrl("https://example.com/profile.jpg");
        personDto.setNationality("American");
        
       response = given()
					.spec(UserAuthMocks.CustomerPrivilegesUser())
					.body(personDto)
					.contentType(ContentType.JSON)
				.when()
		  			.put("/api/v3/persons/{newUserId}/update", newUserId)
		  		.then()
		  			.statusCode(200)
		  		.and()
		  			.extract()
		  			.response();
       
       jsonPath = JsonPath.from(response.getBody().asString());
   	
       assertEquals(personDto.getFirstName(), jsonPath.get("firstName"));
       
       assertEquals(personDto.getLastName(), jsonPath.get("lastName"));
       
       assertEquals(personDto.getGender(), jsonPath.get("gender"));
       
       assertEquals(personDto.getPhone(), jsonPath.get("phone"));
       
       assertEquals(personDto.getProfilePictureUrl(), jsonPath.get("profilePictureUrl"));
       
       assertEquals(personDto.getNationality(), jsonPath.get("nationality"));
       
       assertEquals(userDto.getKey(), jsonPath.getLong("user.id"));
       
       logger.info("test success, when updating a person with valid data and existing person id, the person was updated and the response provided the updated person details");

    }
    
    @Test
    @DirtiesContext
    void givenValidData_whenUpdatingPerson_withoutToken_thenReceiveForbiddenException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidData_whenUpdatingPerson_withoutToken_thenReceiveForbiddenException()  =========>\n");
    	
    	userDto = authService.register(UserMocks.registerUserMockDto());
    	
    	Long newUserId = userDto.getKey();

    	Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("firstName", "John");
        dataMap.put("lastName", "Doe");
    	
    	given()
			.body(dataMap)
			.contentType(ContentType.JSON)
		.when()
	  		.put("/api/v3/persons/{newUserId}/update", newUserId)
	  	.then()
		  	.statusCode(401)
	  		.body("message", equalTo("Unauthorized"));
    	
    	logger.info("test success, when trying to update a person without an token, the response properly provided the exception and message explaining");
    	
    }

    @Test
    @DirtiesContext
    void givenInvalidData_whenUpdatingPerson_thenReceiveInvalidEntityException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenInvalidData_whenUpdatingPerson_thenReceiveInvalidEntityException()  =========>\n");

    	userDto = authService.register(UserMocks.registerUserMockDto());
    	
    	Long newUserId = userDto.getKey();
    	
    	personDto = personService.create(newUserId, PersonMocks.insertPersonMock(userDto));
    	
    	Long newPersonId = personDto.getKey();
    	
    	Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("firstName", "John");
        dataMap.put("lastName", "Doe");
        dataMap.put("gender", "Male");
        dataMap.put("phone", "99876543210"); 
        dataMap.put("nationality", "brazillian");
        dataMap.put("profilePictureUrl", "https://example.com/profile.jpg");
        
        Map<String, Object> invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("setting invalid firstname");
        
        invalidDataMap.put("firstName", "invalid firstname 333");
    	
    	given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidDataMap)
			.contentType(ContentType.JSON)
		.when()
		  	.put("/api/v3/persons/{newUserId}/update", newPersonId)
		.then()
			.statusCode(422)
			.body("message", equalTo("personUpdateDto: campo [firstName] aceita apenas letras. Valor passado: '" + invalidDataMap.get("firstName") + "'"));
    	
    	invalidDataMap = new HashMap<>(dataMap);
    	
    	logger.info("setting invalid lastname");
        
    	invalidDataMap.put("lastName", "invalid lastname 333");
        
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidDataMap)
			.contentType(ContentType.JSON)
		.when()
	  		.put("/api/v3/persons/{newUserId}/update", newPersonId)
	  	.then()
			.statusCode(422)
			.body("message", equalTo("personUpdateDto: campo [lastName] aceita apenas letras. Valor passado: '" + invalidDataMap.get("lastName") + "'"));
        
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("setting invalid gender");
        
        invalidDataMap.put("gender", "invalid gender 333");
        
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidDataMap)
			.contentType(ContentType.JSON)
		.when()
  			.put("/api/v3/persons/{newUserId}/update", newPersonId)
  		.then()
			.statusCode(422)
			.body("message", equalTo("personUpdateDto: campo [gender] aceita apenas letras. Valor passado: '" + invalidDataMap.get("gender") + "'"));
        
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("setting invalid phone");
        
        invalidDataMap.put("phone", "invalid num");
        
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidDataMap)
			.contentType(ContentType.JSON)
		.when()
  			.put("/api/v3/persons/{newUserId}/update", newPersonId)
  		.then()
			.statusCode(422)
			.body("message", equalTo("personUpdateDto: campo [phone] aceita apenas números no formato 99877776666. Valor passado: '" + invalidDataMap.get("phone") + "'"));
        
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("setting invalid phone");
        
        invalidDataMap.put("phone", "99876543210111");
        
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidDataMap)
			.contentType(ContentType.JSON)
		.when()
  			.put("/api/v3/persons/{newUserId}/update", newPersonId)
  		.then()
			.statusCode(422)
			.body("message", equalTo("personUpdateDto: campo [phone] aceita apenas números no formato 99877776666. Valor passado: '" + invalidDataMap.get("phone") + "'"));
        
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("setting invalid nationality");
        
        invalidDataMap.put("nationality", "invalid nationality 333");
        
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidDataMap)
			.contentType(ContentType.JSON)
		.when()
  			.put("/api/v3/persons/{newUserId}/update", newPersonId)
  		.then()
			.statusCode(422)
			.body("message", equalTo("personUpdateDto: campo [nationality] aceita apenas letras. Valor passado: '" + invalidDataMap.get("nationality") + "'")); 
        
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("setting invalid profilePictureUrl");
        
        invalidDataMap.put("profilePictureUrl", "invalid picture url");
        
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidDataMap)
			.contentType(ContentType.JSON)
		.when()
  			.put("/api/v3/persons/{newUserId}/update", newPersonId)
  		.then()
			.statusCode(422)
			.body("message", equalTo("personUpdateDto: campo [profilePictureUrl] deve ser uma URL válida. Valor passado: '" + invalidDataMap.get("profilePictureUrl") + "'")); 
           
        logger.info("test success, when trying to update a person with invalid data, the response properly provided the exception with details");
    	
    }

    @Test
    @DirtiesContext
    void givenValidData_whenAddingAddress_thenCreateAddressAndReturnCreatedAddressDetails() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidData_whenAddingAddress_thenCreateAddressAndReturnCreatedAddressDetails()  =========>\n");
    	
    	userDto = authService.register(UserMocks.registerUserMockDto());
    	
    	Long newUserId = userDto.getKey();
    	
    	personDto = personService.create(newUserId, PersonMocks.insertPersonMock(userDto));
    	
    	Long newPersonId = personDto.getKey();
    	
    	addressDto = new AddressDto();

    	addressDto.setCep("12345678");
    	addressDto.setAddressName("Oak Street");
    	addressDto.setNumber(42);
    	addressDto.setComplement("Unit 202");
    	addressDto.setDistrict("Green Valley");
    	addressDto.setCity("Metropolis");
    	addressDto.setState("SP");

    	response = given()
				.spec(UserAuthMocks.CustomerPrivilegesUser())
				.body(addressDto)
				.contentType(ContentType.JSON)
			.when()
			  	.post("/api/v3/persons/{personId}/addresses/create", newPersonId)
			.then()
				.statusCode(201)
			.and()
				.extract()
				.response();
    	
    	personDto = personService.findById(newPersonId);
    	
    	addressDto = personDto.getAddresses().get(0);
    	
    	jsonPath = JsonPath.from(response.getBody().asString());
    	
    	assertEquals(addressDto.getId(), jsonPath.getLong("id"));
    	
    	assertEquals(addressDto.getCep(), jsonPath.get("cep"));
    	
    	assertEquals(addressDto.getAddressName(), jsonPath.get("addressName"));
    	
    	assertEquals(addressDto.getNumber(), jsonPath.get("number"));
    	
    	assertEquals(addressDto.getComplement(), jsonPath.get("complement"));
    	
    	assertEquals(addressDto.getDistrict(), jsonPath.get("district"));
    	
    	assertEquals(addressDto.getCity(), jsonPath.get("city"));
    	
    	assertEquals(addressDto.getState(), jsonPath.get("state"));
    	
    	logger.info("test success, when trying to insert a new address for a person, the address was created and the response provided the new address details"); 	
    	
    }

    @Test
    void givenValidData_whenAddingAddress_withoutToken_thenReceiveForbiddenException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidData_whenAddingAddress_withoutToken_thenReceiveForbiddenException()  =========>\n");
    	
    	addressDto = new AddressDto();
    	
    	given()
			.body(addressDto)
			.contentType(ContentType.JSON)
		.when()
	  		.post("/api/v3/persons/{personId}/addresses/create", 9999)
	  	.then()
	  		.statusCode(401)
	  		.body("message", equalTo("Unauthorized"));
    	
    	logger.info("test success, when trying to add an person address without a token, the response properly provided the exception and message explaining");
    	
    }
    
    @Test
    @DirtiesContext
    void givenInvalidData_whenAddingAddress_thenReceiveInvalidEntityException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenInvalidData_whenAddingAddress_thenReceiveInvalidEntityException()  =========>\n");
    	
    	userDto = authService.register(UserMocks.registerUserMockDto());
    	
    	Long newUserId = userDto.getKey();
    	
    	personDto = personService.create(newUserId, PersonMocks.insertPersonMock(userDto));
    	
    	Long newPersonId = personDto.getKey();
    	
		Map<String, Object> addressMap = new HashMap<>();
		 
	    addressMap.put("cep", "12345678");
	    addressMap.put("addressName", "Oak Street");
	    addressMap.put("number", 42);
	    addressMap.put("complement", "Unit 202");
	    addressMap.put("district", "Green Valley");
	    addressMap.put("city", "Metropolis");
	    addressMap.put("state", "SP");
    	
	    Map<String, Object> invalidAddressMap = new HashMap<>(addressMap);
        
        logger.info("setting invalid cep");
        
        invalidAddressMap.put("cep", "invalid cep input");
    	
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidAddressMap)
			.contentType(ContentType.JSON)
		.when()
	  		.post("/api/v3/persons/{personId}/addresses/create", newPersonId)
	  	.then()
			.statusCode(422)
			.body("message", equalTo("addressDto: campo [cep] aceita apenas números no formato 11222333. Valor passado: '" + invalidAddressMap.get("cep") + "'"));
        
        invalidAddressMap = new HashMap<>(addressMap);
        
        logger.info("setting invalid lenght cep");
        
        invalidAddressMap.put("cep", "112223334");
    	
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidAddressMap)
			.contentType(ContentType.JSON)
		.when()
	  		.post("/api/v3/persons/{personId}/addresses/create", newPersonId)
	  	.then()
			.statusCode(422)
			.body("message", equalTo("addressDto: campo [cep] aceita apenas números no formato 11222333. Valor passado: '" + invalidAddressMap.get("cep") + "'"));
        
        invalidAddressMap = new HashMap<>(addressMap);
        
        logger.info("setting invalid addressName");
        
        invalidAddressMap.put("addressName", "invalid address name !@#$%¨&*()");
    	
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidAddressMap)
			.contentType(ContentType.JSON)
		.when()
	  		.post("/api/v3/persons/{personId}/addresses/create", newPersonId)
	  	.then()
			.statusCode(422)
			.body("message", equalTo("addressDto: campo [addressName] aceita apenas números e letras. Valor passado: '" + invalidAddressMap.get("addressName") + "'"));
        
        invalidAddressMap = new HashMap<>(addressMap);
        
        logger.info("setting not parsable number");
        
        invalidAddressMap.put("number", "invalid number");
    	
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidAddressMap)
			.contentType(ContentType.JSON)
		.when()
	  		.post("/api/v3/persons/{personId}/addresses/create", newPersonId)
	  	.then()
			.statusCode(400)
			.body("message", containsString("JSON parse error"));
        
        invalidAddressMap = new HashMap<>(addressMap);
        
        logger.info("setting invalid lenght number");
        
        invalidAddressMap.put("number", "9999999");
    	
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidAddressMap)
			.contentType(ContentType.JSON)
		.when()
	  		.post("/api/v3/persons/{personId}/addresses/create", newPersonId)
	  	.then()
			.statusCode(422)
			.body("message", equalTo("addressDto: campo [number] deve ser menor que ou igual à 9999. Valor passado: '" + invalidAddressMap.get("number") + "'"));
        
        invalidAddressMap = new HashMap<>(addressMap);
        
        logger.info("setting invalid complement");
        
        invalidAddressMap.put("complement", "invalid complement !@#$%¨&*()");
    	
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidAddressMap)
			.contentType(ContentType.JSON)
		.when()
	  		.post("/api/v3/persons/{personId}/addresses/create", newPersonId)
	  	.then()
			.statusCode(422)
			.body("message", equalTo("addressDto: campo [complement] aceita apenas números e letras. Valor passado: '" + invalidAddressMap.get("complement") + "'"));
        
        invalidAddressMap = new HashMap<>(addressMap);
        
        logger.info("setting invalid district");
        
        invalidAddressMap.put("district", "invalid district !@#$%¨&*()");
    	
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidAddressMap)
			.contentType(ContentType.JSON)
		.when()
	  		.post("/api/v3/persons/{personId}/addresses/create", newPersonId)
	  	.then()
			.statusCode(422)
			.body("message", equalTo("addressDto: campo [district] aceita apenas números e letras. Valor passado: '" + invalidAddressMap.get("district") + "'"));
        
        invalidAddressMap = new HashMap<>(addressMap);
        
        logger.info("setting invalid city");
        
        invalidAddressMap.put("city", "invalid city 333");
    	
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidAddressMap)
			.contentType(ContentType.JSON)
		.when()
	  		.post("/api/v3/persons/{personId}/addresses/create", newPersonId)
	  	.then()
			.statusCode(422)
			.body("message", equalTo("addressDto: campo [city] aceita apenas letras. Valor passado: '" + invalidAddressMap.get("city") + "'"));
        
        invalidAddressMap = new HashMap<>(addressMap);
        
        logger.info("setting invalid state");
        
        invalidAddressMap.put("state", "AA");
    	
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidAddressMap)
			.contentType(ContentType.JSON)
		.when()
	  		.post("/api/v3/persons/{personId}/addresses/create", newPersonId)
	  	.then()
			.statusCode(422)
			.body("message", equalTo("addressDto: campo [state] deve ser um código válido de UF. Valor passado: '" + invalidAddressMap.get("state") + "'"));
        
        invalidAddressMap = new HashMap<>(addressMap);
        
        logger.info("test success, when trying to add a person address with invalid data, the response properly provided the exception with details");  
    	
    }

    @Test
    @DirtiesContext
    void givenValidData_whenUpdatingAddress_thenUpdateAdderessAndReturnUpdatedAddressDetails() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidData_whenUpdatingAddress_thenUpdateAdderessAndReturnUpdatedAddressDetails()  =========>\n");
    	
    	userDto = authService.register(UserMocks.registerUserMockDto());
    	
    	Long newUserId = userDto.getKey();
    	
    	personDto = personService.create(newUserId, PersonMocks.insertPersonMock(userDto));
    	
    	Long newPersonId = personDto.getKey();
    	
    	addressDto = personService.addAddress(newPersonId, PersonMocks.insertAddressMock(personDto));
    	
    	personDto = personService.findById(newPersonId);
    	
    	addressDto = personDto.getAddresses().get(0);
    	
    	Long newAddressId = addressDto.getId();
    	
    	response = given()
				.spec(UserAuthMocks.CustomerPrivilegesUser())
				.body(addressDto)
				.contentType(ContentType.JSON)
			.when()
			  	.put("/api/v3/persons/{personId}/addresses/{addressId}/update", newPersonId, newAddressId)
			.then()
				.statusCode(200)
			.and()
				.extract()
				.response();
    	
    	jsonPath = JsonPath.from(response.getBody().asString());
    	
    	assertEquals(addressDto.getId(), newAddressId);
    	
    	assertEquals(addressDto.getCep(), jsonPath.get("cep"));
    	
    	assertEquals(addressDto.getAddressName(), jsonPath.get("addressName"));
    	
    	assertEquals(addressDto.getNumber(), jsonPath.get("number"));
    	
    	assertEquals(addressDto.getComplement(), jsonPath.get("complement"));
    	
    	assertEquals(addressDto.getDistrict(), jsonPath.get("district"));
    	
    	assertEquals(addressDto.getCity(), jsonPath.get("city"));
    	
    	assertEquals(addressDto.getState(), jsonPath.get("state"));
    	
    	logger.info("test success, when trying to update a address with valid data, the address was updated and the response provided the updated address details");
    	
    }
    
    @Test
    void givenValidData_whenUpdatingAddress_withoutToken_thenReceiveForbiddenException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidData_whenUpdatingAddress_withoutToken_thenReceiveForbiddenException()  =========>\n");
    	
    	addressDto = new AddressDto();
    	
    	given()
			.body(addressDto)
			.contentType(ContentType.JSON)
		.when()
	  		.put("/api/v3/persons/{personId}/addresses/{addressId}/update", 9999, 9999)
	  	.then()
	  		.statusCode(401)
	  		.body("message", equalTo("Unauthorized"));
    	
    	logger.info("test success, when trying to update an person address without a token, the response properly provided the exception and message explaining");
    	
    }
    
    @Test
    @DirtiesContext
    void givenInvalidData_whenUpdatingAddress_thenReceiveInvalidEntityException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenInvalidData_whenUpdatingAddress_thenReceiveInvalidEntityException()  =========>\n");
    	
    	userDto = authService.register(UserMocks.registerUserMockDto());
    	
    	Long newUserId = userDto.getKey();
    	
    	personDto = personService.create(newUserId, PersonMocks.insertPersonMock(userDto));
    	
    	Long newPersonId = personDto.getKey();
    	
    	addressDto = personService.addAddress(newPersonId, PersonMocks.insertAddressMock(personDto));
    	
    	Long newAddressId = addressDto.getId();
    	
    	Map<String, Object> addressMap = new HashMap<>();
		 
	    addressMap.put("cep", addressDto.getCep());
	    addressMap.put("addressName", addressDto.getAddressName());
	    addressMap.put("number", addressDto.getNumber());
	    addressMap.put("complement", addressDto.getComplement());
	    addressMap.put("district", addressDto.getDistrict());
	    addressMap.put("city", addressDto.getCity());
	    addressMap.put("state", addressDto.getState());
	    
	    Map<String, Object> invalidAddressMap = new HashMap<>(addressMap);
	    
	    logger.info("setting invalid addressName");
        
        invalidAddressMap.put("addressName", "invalid address name !@#$%¨&*()");
    	
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidAddressMap)
			.contentType(ContentType.JSON)
		.when()
	  		.put("/api/v3/persons/{personId}/addresses/{addressId}/update", newPersonId, newAddressId)
	  	.then()
			.statusCode(422)
			.body("message", equalTo("addressDto: campo [addressName] aceita apenas números e letras. Valor passado: '" + invalidAddressMap.get("addressName") + "'"));
        
        invalidAddressMap = new HashMap<>(addressMap);
        
        logger.info("setting invalid complement");
        
        invalidAddressMap.put("complement", "invalid complement name !@#$%¨&*()");
    	
        given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(invalidAddressMap)
			.contentType(ContentType.JSON)
		.when()
	  		.put("/api/v3/persons/{personId}/addresses/{addressId}/update", newPersonId, newAddressId)
	  	.then()
			.statusCode(422)
			.body("message", equalTo("addressDto: campo [complement] aceita apenas números e letras. Valor passado: '" + invalidAddressMap.get("complement") + "'"));
        
        logger.info("test success, when trying to update a person address with invalid data, the response properly provided the exception with details");  
    	
    }
    
    @Test
    @DirtiesContext
    void givenValidData_withInvalidPersonId_thenReceiveInvalidPersonForAddressUpdateException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenInvalidData_whenUpdatingAddress_thenReceiveInvalidEntityException()  =========>\n");
    	
    	userDto = authService.register(UserMocks.registerUserMockDto());
    	
    	Long newUserId = userDto.getKey();
    	
    	personDto = personService.create(newUserId, PersonMocks.insertPersonMock(userDto));
    	
    	Long newPersonId = personDto.getKey();
    	
    	addressDto = personService.addAddress(newPersonId, PersonMocks.insertAddressMock(personDto));
    	
    	Long newAddressId = addressDto.getId();
    	
    	Long randomPersonId = 1L;
    	
    	given()
			.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(addressDto)
			.contentType(ContentType.JSON)
		.when()
  			.put("/api/v3/persons/{personId}/addresses/{addressId}/update", randomPersonId, newAddressId)
  		.then()
			.statusCode(400)
			.body("message", equalTo("Address ID provided doesnt belong to the person with ID " + randomPersonId));
    	
    	logger.info("test success, when trying to update an address providing person id not related to this address, the response properly provided the exception and message explaining");
    	
    }
}
