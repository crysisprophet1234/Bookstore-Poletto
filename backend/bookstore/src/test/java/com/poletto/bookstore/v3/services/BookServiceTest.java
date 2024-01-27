package com.poletto.bookstore.v3.services;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.filters;
import static io.restassured.RestAssured.port;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static io.restassured.RestAssured.given;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.PropertySource;
import org.springframework.hateoas.Link;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.poletto.bookstore.dto.v3.AuthorDto;
import com.poletto.bookstore.dto.v3.BookDto;
import com.poletto.bookstore.dto.v3.BookStatusUpdateDto;
import com.poletto.bookstore.dto.v3.CategoryDto;
import com.poletto.bookstore.entities.enums.BookStatus;
import com.poletto.bookstore.repositories.v3.BookRepository;
import com.poletto.bookstore.services.v3.impl.BookServiceImpl;
import com.poletto.bookstore.v3.mocks.BookMocks;
import com.poletto.bookstore.v3.mocks.UserAuthMocks;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@PropertySource("classpath:application-test.properties")
public class BookServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(BookServiceTest.class);
	
	@LocalServerPort
	private int serverPort;
	
	@Value("${server.test.url}")
	private String serverBaseURI;
	
	private Response response;
	private JsonPath jsonPath;
	private Map<String, String> json = new HashMap<>();
	
	@SpyBean
	private BookRepository bookRepository;
	
	@Autowired
	@InjectMocks
	private BookServiceImpl bookService;
	
	private static BookDto bookDto;
	
	private static AuthorDto authorDto;
 
    @BeforeEach
    public void setUp() {
    	
    	baseURI = serverBaseURI;
        port = serverPort;
        useRelaxedHTTPSValidation();
        filters(new RequestLoggingFilter(), new ResponseLoggingFilter()); 
	
    }
    
    @AfterEach
    public void tearDown() {
    	
    	bookDto = null;
    	
    	authorDto = null;
    	
    	response = null;
    	jsonPath = null;
    	json.clear();
    	
    }
    
    @Test
    void givenExistingBookId_whenRequestingBookById_thenReceiveBookDetails() {

        logger.info("\n\n<=========  STARTING TEST givenExistingBookId_whenRequestingBookById_thenReceiveBookDetails()  =========>\n");

        long bookId = 1L;

        bookDto = bookService.findById(bookId);

        response = given()
		            .when()
		                .get("/api/v3/books/{bookId}", bookId)
		            .then()
		                .extract().response();

        jsonPath = JsonPath.from(response.getBody().asString());

        assertEquals(200, response.getStatusCode());

        assertEquals(bookDto.getId(), jsonPath.getLong("id"));
        
        assertEquals(bookDto.getTitle(), jsonPath.getString("title"));
        
        assertEquals(bookDto.getDescription(), jsonPath.getString("description"));
        
        assertEquals(bookDto.getLanguage(), jsonPath.getString("language"));
        
        assertEquals(bookDto.getNumberOfPages(), jsonPath.getInt("numberOfPages"));
        
        assertEquals(bookDto.getPublicationDate().toString(), jsonPath.getString("publicationDate"));
        
        assertEquals(bookDto.getImgUrl(), jsonPath.getString("imgUrl"));
        
        assertEquals(bookDto.getStatus().toString(), jsonPath.getString("status"));

        assertEquals(bookDto.getAuthor().getId(), jsonPath.getLong("author.id"));
        
        assertEquals(bookDto.getAuthor().getName(), jsonPath.getString("author.name"));

        for (int i = 0; i < bookDto.getCategories().size(); i++) {
        	
            String categoryPath = "categories[" + i + "]";
            
            CategoryDto categoryDto = bookDto.getCategories().stream().collect(Collectors.toList()).get(i);

            assertEquals(categoryDto.getId(), jsonPath.getLong(categoryPath + ".id"));
            
            assertEquals(categoryDto.getName(), jsonPath.getString(categoryPath + ".name"));

        }

        List<Link> linkDtos = bookDto.getLinks().toList();
		
		for (int i = 0; i < linkDtos.size(); i++) {
			
		    String linkPath = "links[" + i + "]";
		    
		    Link link = linkDtos.get(i);

		    assertEquals(link.getRel().toString(), jsonPath.getString(linkPath + ".rel"));
		    assertEquals(link.getType().toString(), jsonPath.getString(linkPath + ".type"));
		    assertThat((jsonPath.getString(linkPath + ".href")).contains(link.getHref().toString()));
		    
		}

        logger.info("Test success, when the user requested a book details by its id, the response properly provided the details");

    }
    
    @Test
    void givenNonExistingBookId_whenRequestingBookById_thenReceiveNotFoundException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenNonExistingBookId_whenRequestingBookById_thenReceiveNotFoundException()  =========>\n");
    	
    	Long bookId = 99999L;
    	
    	given()
		.when()
		  	.get("/api/v3/books/{bookId}", bookId)
		.then()
		  	.statusCode(404)
		.body("message", equalTo("Resource BOOK not found. ID " + bookId));
		
		logger.info("test success, when the user requested a book detail with a non existing book id, the response properly provided the not found exception with message explaining");
    	
    }
    
    @Test
    @DirtiesContext
    void givenValidData_whenCreatingBook_thenCreateBookAndReturnBookDetails() {

        logger.info("\n\n<=========  STARTING TEST givenValidData_whenCreatingBook_thenCreateBookAndReturnBookDetails()  =========>\n");
        
        authorDto = BookMocks.authorMockDto();

        Long authorId = authorDto.getId();

        List<Map<String, Long>> categoryIds = List.of(
    		Map.of("id", BookMocks.categoryMockDto(1L).getId()),
    		Map.of("id", BookMocks.categoryMockDto(2L).getId())
    	);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("title", "The Hitchhiker's Guide to the Galaxy");
        dataMap.put("description", "A hilarious and thought-provoking space adventure.");
        dataMap.put("language", "English");
        dataMap.put("numberOfPages", 420);
        dataMap.put("publicationDate", "1979-10-12");
        dataMap.put("imgUrl", "https://example.com/book-cover.jpg");
        dataMap.put("author", Map.of("id", authorId));
        dataMap.put("categories", categoryIds);

        response = given()
                .spec(UserAuthMocks.OperatorPrivilegesUser())
                .body(dataMap)
                .contentType(ContentType.JSON)
            .when()
                .post("/api/v3/books/create")
            .then()
                .statusCode(201)
            .and()
                .extract()
                .response();

        jsonPath = JsonPath.from(response.getBody().asString());
        
        bookDto = bookService.findById(jsonPath.getLong("id"));

        assertEquals(dataMap.get("title"), jsonPath.get("title"));
        
        assertEquals(dataMap.get("description"), jsonPath.get("description"));
        
        assertEquals(dataMap.get("language"), jsonPath.get("language"));
        
        assertEquals(dataMap.get("numberOfPages"), jsonPath.getInt("numberOfPages"));
        
        assertEquals(dataMap.get("publicationDate"), jsonPath.getString("publicationDate"));
        
        assertEquals(dataMap.get("imgUrl"), jsonPath.getString("imgUrl"));
        
        assertEquals(BookStatus.ACTIVE.toString(), jsonPath.getString("status"));

        assertEquals(bookDto.getAuthor().getId(), jsonPath.getLong("author.id"));
        
        assertEquals(bookDto.getAuthor().getName(), jsonPath.getString("author.name"));
        
        for (int i = 0; i < bookDto.getCategories().size(); i++) {
        	
            String categoryPath = "categories[" + i + "]";
            
            CategoryDto categoryDto = bookDto.getCategories().stream().collect(Collectors.toList()).get(i);

            assertEquals(categoryDto.getId(), jsonPath.getLong(categoryPath + ".id"));
            
            assertEquals(categoryDto.getName(), jsonPath.getString(categoryPath + ".name"));

        }
        
        List<Link> linkDtos = bookDto.getLinks().toList();
		
		for (int i = 0; i < linkDtos.size(); i++) {
			
		    String linkPath = "links[" + i + "]";
		    
		    Link link = linkDtos.get(i);

		    assertEquals(link.getRel().toString(), jsonPath.getString(linkPath + ".rel"));
		    assertEquals(link.getType().toString(), jsonPath.getString(linkPath + ".type"));
		    assertThat((jsonPath.getString(linkPath + ".href")).contains(link.getHref().toString()));
		    
		}

        logger.info("Test success, when creating a book with valid data, the book was created and the response provided the new book details");

    }
    
    @Test
    @DirtiesContext
    void givenValidData_whenCreatingBook_withoutToken_thenReceiveUnauthorizedException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidData_whenCreatingBook_withoutToken_thenReceiveUnauthorizedException()  =========>\n");
    	
    	 Map<String, Object> dataMap = new HashMap<>();
    	 
    	 given()
			.body(dataMap)
			.contentType(ContentType.JSON)
		.when()
	  		.post("/api/v3/books/create")
	  	.then()
		  	.statusCode(401)
	  		.body("message", equalTo("Unauthorized"));
 	
    	 logger.info("test success, when trying to create new person without an token, the response properly provided the exception and message explaining");
    	
    }
    
    @Test
    @DirtiesContext
    void givenValidData_whenCreatingBook_withoutNecessaryRole_thenReceiveForbiddenException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidData_whenCreatingBook_withoutNecessaryRole_thenReceiveForbiddenException()  =========>\n");
    	
    	 Map<String, Object> dataMap = new HashMap<>();
    	 
    	 given()
    	 	.spec(UserAuthMocks.CustomerPrivilegesUser())
			.body(dataMap)
			.contentType(ContentType.JSON)
		.when()
	  		.post("/api/v3/books/create")
	  	.then()
		  	.statusCode(403)
	  		.body("message", equalTo("Access Denied"));
 	
    	 logger.info("test success, when trying to create new person with a customer token, the response properly provided the exception and message explaining");
    	
    }

    @Test
    @DirtiesContext
    void givenInvalidData_whenCreatingBook_thenReceiveInvalidEntityException() {

    	logger.info("\n\n<=========  STARTING TEST givenInvalidData_whenCreatingBook_thenReceiveInvalidEntityException()  =========>\n");
    	
    	authorDto = BookMocks.authorMockDto();

        Long authorId = authorDto.getId();

        List<Map<String, Long>> categoryIds = List.of(
    		Map.of("id", BookMocks.categoryMockDto(1L).getId()),
    		Map.of("id", BookMocks.categoryMockDto(2L).getId())
    	);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("title", "The Hitchhiker's Guide to the Galaxy");
        dataMap.put("description", "A hilarious and thought-provoking space adventure.");
        dataMap.put("language", "English");
        dataMap.put("numberOfPages", 420);
        dataMap.put("publicationDate", "1979-10-12");
        dataMap.put("imgUrl", "https://example.com/book-cover.jpg");
        dataMap.put("author", Map.of("id", authorId));
        dataMap.put("categories", categoryIds);

        Map<String, Object> invalidDataMap = new HashMap<>(dataMap);

        logger.info("Setting invalid title");
        invalidDataMap.put("title", "invalid title 123!#$");
        testCreateBookWithInvalidData(invalidDataMap, "bookDto: campo [title] aceita apenas letras e números. Valor passado: '" + invalidDataMap.get("title") + "'");
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("Setting invalid description");
        invalidDataMap.put("description", "too short");
        testCreateBookWithInvalidData(invalidDataMap, "bookDto: campo [description] tamanho deve ser entre 10 e 1000. Valor passado: '" + invalidDataMap.get("description") + "'");  
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("Setting invalid language");
        invalidDataMap.put("language", "invalid language 123");
        testCreateBookWithInvalidData(invalidDataMap, "bookDto: campo [language] aceita apenas letras. Valor passado: '" + invalidDataMap.get("language") + "'");  
        invalidDataMap = new HashMap<>(dataMap);

        logger.info("Setting invalid numberOfPages");
        invalidDataMap.put("numberOfPages", 2);
        testCreateBookWithInvalidData(invalidDataMap, "bookDto: campo [numberOfPages] deve ser maior que ou igual à 5. Valor passado: '" + invalidDataMap.get("numberOfPages") + "'");  
        invalidDataMap = new HashMap<>(dataMap);

        logger.info("Setting invalid numberOfPages");
        invalidDataMap.put("numberOfPages", 6000);
        testCreateBookWithInvalidData(invalidDataMap, "bookDto: campo [numberOfPages] deve ser menor que ou igual à 5000. Valor passado: '" + invalidDataMap.get("numberOfPages") + "'");  
        invalidDataMap = new HashMap<>(dataMap);

        logger.info("Setting invalid publicationDate (future)");
        invalidDataMap.put("publicationDate", "2099-12-31");
        testCreateBookWithInvalidData(invalidDataMap, "bookDto: campo [publicationDate] deve ser uma data no passado ou no presente. Valor passado: '" + invalidDataMap.get("publicationDate") + "'");  
        invalidDataMap = new HashMap<>(dataMap);

        logger.info("Setting invalid imgUrl");       
        invalidDataMap.put("imgUrl", "invalid url");
        testCreateBookWithInvalidData(invalidDataMap, "bookDto: campo [imgUrl] deve ser uma URL válida. Valor passado: '" + invalidDataMap.get("imgUrl") + "'");  
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("Setting null author");
        invalidDataMap.put("author", null);
        testCreateBookWithInvalidData(invalidDataMap, "bookDto: campo [author] não deve ser nulo. Valor passado: (null)");  
        invalidDataMap = new HashMap<>(dataMap);

        logger.info("Setting null categories");
        invalidDataMap.put("categories", null);
        testCreateBookWithInvalidData(invalidDataMap, "bookDto: campo [categories] não deve estar vazio. Valor passado: (null)");  
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("Setting empty categories");
        invalidDataMap.put("categories", Set.of());
        testCreateBookWithInvalidData(invalidDataMap, "bookDto: campo [categories] não deve estar vazio. Valor passado: '[]'");
        invalidDataMap = new HashMap<>(dataMap);

        logger.info("Test success, when trying to create book with invalid data, the response properly provided the exception with details");

    }
    
    @Test
    @DirtiesContext
    void givenValidData_whenUpdatingBook_thenUpdateBookAndReturnUpdatedBookDetails() {
    	
    	bookDto = bookService.insert(BookMocks.insertBookMockDto());
    	
    	Long newBookId = bookDto.getId();
    	
    	BookDto updatedBookDto = new BookDto();
    	
    	updatedBookDto.setId(newBookId);
    	updatedBookDto.setTitle("The Hitchhiker's Guide to the Galaxy");
    	updatedBookDto.setDescription("A hilarious and thought-provoking space adventure.");
    	updatedBookDto.setLanguage("English");
    	updatedBookDto.setNumberOfPages(420);
    	updatedBookDto.setPublicationDate(LocalDate.of(1979, 10, 12));
    	updatedBookDto.setImgUrl("https://example.com/book-cover.jpg");
    	updatedBookDto.setAuthor(BookMocks.authorMockDto(3L));
    	updatedBookDto.getCategories().add(BookMocks.categoryMockDto(5L));
    	
    	response = given()
	                .spec(UserAuthMocks.OperatorPrivilegesUser())
	                .body(updatedBookDto)
	                .contentType(ContentType.JSON)
	            .when()
	                .put("/api/v3/books/{bookId}/update", newBookId)
	            .then()
	                .statusCode(200)
	            .extract()
	                .response();
    	
    	 jsonPath = JsonPath.from(response.getBody().asString());
    	 
    	 bookDto = bookService.findById(newBookId);
    	 
    	 assertEquals(bookDto.getId(), jsonPath.getLong("id"));
         
         assertEquals(bookDto.getTitle(), jsonPath.getString("title"));
         
         assertEquals(bookDto.getDescription(), jsonPath.getString("description"));
         
         assertEquals(bookDto.getLanguage(), jsonPath.getString("language"));
         
         assertEquals(bookDto.getNumberOfPages(), jsonPath.getInt("numberOfPages"));
         
         assertEquals(bookDto.getPublicationDate().toString(), jsonPath.getString("publicationDate"));
         
         assertEquals(bookDto.getImgUrl(), jsonPath.getString("imgUrl"));
         
         assertEquals(bookDto.getStatus().toString(), jsonPath.getString("status"));

         assertEquals(bookDto.getAuthor().getId(), jsonPath.getLong("author.id"));
         
         assertEquals(bookDto.getAuthor().getName(), jsonPath.getString("author.name"));

         for (int i = 0; i < bookDto.getCategories().size(); i++) {
         	
             String categoryPath = "categories[" + i + "]";
             
             CategoryDto categoryDto = bookDto.getCategories().stream().collect(Collectors.toList()).get(i);

             assertEquals(categoryDto.getId(), jsonPath.getLong(categoryPath + ".id"));
             
             assertEquals(categoryDto.getName(), jsonPath.getString(categoryPath + ".name"));

         }

         List<Link> linkDtos = bookDto.getLinks().toList();
 		
 		for (int i = 0; i < linkDtos.size(); i++) {
 			
 		    String linkPath = "links[" + i + "]";
 		    
 		    Link link = linkDtos.get(i);

 		    assertEquals(link.getRel().toString(), jsonPath.getString(linkPath + ".rel"));
 		    assertEquals(link.getType().toString(), jsonPath.getString(linkPath + ".type"));
 		    assertThat((jsonPath.getString(linkPath + ".href")).contains(link.getHref().toString()));
 		    
 		}
 		
 		logger.info("test success, when updating a book with valid data and existing book id, the book was updated and the response provided the updated book details");
     	
    }
    
    @Test
    @DirtiesContext
    void givenValidData_whenUpdatingBook_withoutToken_thenReceiveUnauthorizedException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidData_whenUpdatingBook_withoutToken_thenReceiveUnauthorizedException()  =========>\n");
    	
	   	 Map<String, Object> dataMap = new HashMap<>();
	   	 
	   	 given()
				.body(dataMap)
				.contentType(ContentType.JSON)
			.when()
		  		.put("/api/v3/books/{bookId}/update", 9999L)
		  	.then()
			  	.statusCode(401)
		  		.body("message", equalTo("Unauthorized"));
		
	   	 logger.info("test success, when trying to update a new person without an token, the response properly provided the exception and message explaining");
    	
    }
    
    @Test
    @DirtiesContext
    void givenValidData_whenUpdatingBook_withoutNecessaryRole_thenReceiveForbiddenException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenValidData_whenUpdatingBook_withoutNecessaryRole_thenReceiveForbiddenException()  =========>\n");
    	
	   	 Map<String, Object> dataMap = new HashMap<>();
	   	 
	   	 given()
	   	 		.spec(UserAuthMocks.CustomerPrivilegesUser())
				.body(dataMap)
				.contentType(ContentType.JSON)
			.when()
			.put("/api/v3/books/{bookId}/update", 9999L)
		  	.then()
			  	.statusCode(403)
		  		.body("message", equalTo("Access Denied"));
		
	   	 logger.info("test success, when trying to update a new person without necessary role, the response properly provided the exception and message explaining");
    	
    }

    @Test
    @DirtiesContext
    void givenInvalidData_whenUpdatingBook_thenReceiveInvalidEntityException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenInvalidData_whenUpdatingBook_thenReceiveInvalidEntityException()  =========>\n");
    	
    	authorDto = BookMocks.authorMockDto();

        Long authorId = authorDto.getId();

        List<Map<String, Long>> categoryIds = List.of(
    		Map.of("id", BookMocks.categoryMockDto(1L).getId()),
    		Map.of("id", BookMocks.categoryMockDto(2L).getId())
    	);

    	bookDto = bookService.insert(BookMocks.insertBookMockDto());
    	
    	Long newBookId = bookDto.getId();
    	
    	Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("title", "The Hitchhiker's Guide to the Galaxy");
        dataMap.put("description", "A hilarious and thought-provoking space adventure.");
        dataMap.put("language", "English");
        dataMap.put("numberOfPages", 420);
        dataMap.put("publicationDate", "1979-10-12");
        dataMap.put("imgUrl", "https://example.com/book-cover.jpg");
        dataMap.put("author", Map.of("id", authorId));
        dataMap.put("categories", categoryIds);

        Map<String, Object> invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("Setting invalid title");
        invalidDataMap.put("title", "invalid title 123!#$");
        testUpdateBookWithInvalidData(newBookId, invalidDataMap, "bookUpdateDto: campo [title] aceita apenas letras e números. Valor passado: '" + invalidDataMap.get("title") + "'");
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("Setting invalid description");
        invalidDataMap.put("description", "too short");
        testUpdateBookWithInvalidData(newBookId, invalidDataMap, "bookUpdateDto: campo [description] tamanho deve ser entre 10 e 1000. Valor passado: '" + invalidDataMap.get("description") + "'");  
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("Setting invalid language");
        invalidDataMap.put("language", "invalid language 123");
        testUpdateBookWithInvalidData(newBookId, invalidDataMap, "bookUpdateDto: campo [language] aceita apenas letras. Valor passado: '" + invalidDataMap.get("language") + "'");  
        invalidDataMap = new HashMap<>(dataMap);

        logger.info("Setting invalid numberOfPages");
        invalidDataMap.put("numberOfPages", 2);
        testUpdateBookWithInvalidData(newBookId, invalidDataMap, "bookUpdateDto: campo [numberOfPages] deve ser maior que ou igual à 5. Valor passado: '" + invalidDataMap.get("numberOfPages") + "'");  
        invalidDataMap = new HashMap<>(dataMap);

        logger.info("Setting invalid numberOfPages");
        invalidDataMap.put("numberOfPages", 6000);
        testUpdateBookWithInvalidData(newBookId, invalidDataMap, "bookUpdateDto: campo [numberOfPages] deve ser menor que ou igual à 5000. Valor passado: '" + invalidDataMap.get("numberOfPages") + "'");  
        invalidDataMap = new HashMap<>(dataMap);

        logger.info("Setting invalid publicationDate (future)");
        invalidDataMap.put("publicationDate", "2099-12-31");
        testUpdateBookWithInvalidData(newBookId, invalidDataMap, "bookUpdateDto: campo [publicationDate] deve ser uma data no passado ou no presente. Valor passado: '" + invalidDataMap.get("publicationDate") + "'");  
        invalidDataMap = new HashMap<>(dataMap);

        logger.info("Setting invalid imgUrl");       
        invalidDataMap.put("imgUrl", "invalid url");
        testUpdateBookWithInvalidData(newBookId, invalidDataMap, "bookUpdateDto: campo [imgUrl] deve ser uma URL válida. Valor passado: '" + invalidDataMap.get("imgUrl") + "'");  
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("Setting null author");
        invalidDataMap.put("author", null);
        testUpdateBookWithInvalidData(newBookId, invalidDataMap, "bookUpdateDto: campo [author] não deve ser nulo. Valor passado: (null)");  
        invalidDataMap = new HashMap<>(dataMap);

        logger.info("Setting null categories");
        invalidDataMap.put("categories", null);
        testUpdateBookWithInvalidData(newBookId, invalidDataMap, "bookUpdateDto: campo [categories] não deve estar vazio. Valor passado: (null)");  
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("Setting empty categories");
        invalidDataMap.put("categories", Set.of());
        testUpdateBookWithInvalidData(newBookId, invalidDataMap, "bookUpdateDto: campo [categories] não deve estar vazio. Valor passado: '[]'");
        invalidDataMap = new HashMap<>(dataMap);
        
        logger.info("Test success, when trying to update book with invalid data, the response properly provided the exception with details");
    	
    }
    
    @Test
    @DirtiesContext
    void givenExistingBookIdAndValidStatus_whenUpdatingBookStatus_thenUpdateBookStatusAndReturnBookDetails() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenExistingBookIdAndValidStatus_whenUpdatingBookStatus_thenUpdateBookStatusAndReturnBookDetails()  =========>\n");
    	
    	bookDto = bookService.insert(BookMocks.insertBookMockDto());
    	
    	Long newBookId = bookDto.getId();
    	
    	BookStatusUpdateDto bookStatusUpdateDto = new BookStatusUpdateDto(BookStatus.INACTIVE);
    	
    	given()
	    	.spec(UserAuthMocks.OperatorPrivilegesUser())
	        .body(bookStatusUpdateDto)
	        .contentType(ContentType.JSON)
	    .when()
         	.put("/api/v3/books/{bookId}/change-status", newBookId)
        .then()
	        .statusCode(200)
	        .body("status", equalTo(bookStatusUpdateDto.getBookStatus().toString()));
    	
    	bookStatusUpdateDto.setBookStatus(BookStatus.ACTIVE);
    	
    	given()
	    	.spec(UserAuthMocks.OperatorPrivilegesUser())
	        .body(bookStatusUpdateDto)
	        .contentType(ContentType.JSON)
	    .when()
         	.put("/api/v3/books/{bookId}/change-status", newBookId)
        .then()
	        .statusCode(200)
	        .body("status", equalTo(bookStatusUpdateDto.getBookStatus().toString()));
  
    	logger.info("test success, when the user requested to change a book status with a existing book id and valid status, the book status got properly updated and returned the book details");
    	
    }
    
    @Test
    @DirtiesContext
    void givenExistingBookIdAndAlreadySettedStatus_whenUpdatingBookStatus_thenReceiveInvalidStatusException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenExistingBookIdAndAlreadySettedStatus_whenUpdatingBookStatus_thenReceiveInvalidStatusException()  =========>\n");
    	
    	bookDto = bookService.insert(BookMocks.insertBookMockDto());
    	
    	Long newBookId = bookDto.getId();
    	
    	BookStatusUpdateDto bookStatusUpdateDto = new BookStatusUpdateDto(bookDto.getStatus());
    	
    	given()
	    	.spec(UserAuthMocks.OperatorPrivilegesUser())
	        .body(bookStatusUpdateDto)
	        .contentType(ContentType.JSON)
	    .when()
         	.put("/api/v3/books/{bookId}/change-status", newBookId)
        .then()
	        .statusCode(400)
	        .body("message", equalTo("Status inválido para o livro ID " + bookDto.getId() + ", permanecerá o status " + bookDto.getStatus()));
    	
    	logger.info("test success, when the user requested to change a book status with a existing book id and already setted status, the response properly provided the exception with message explaining");
    	
    }
    
    @Test
    @DirtiesContext
    void givenExistingBookIdAndAlreadyInvalidStatus_whenUpdatingBookStatus_thenReceiveInvalidStatusException() {
    	
    	logger.info("\n\n<=========  STARTING TEST givenExistingBookIdAndAlreadyInvalidStatus_whenUpdatingBookStatus_thenReceiveInvalidStatusException()  =========>\n");
    	
    	bookDto = bookService.insert(BookMocks.insertBookMockDto());
    	
    	Long newBookId = bookDto.getId();
    	
    	String invalidStatus = "INVALID STATUS VALUE";
    	
    	given()
	    	.spec(UserAuthMocks.OperatorPrivilegesUser())
	        .body("{\"bookStatus\": \"" + invalidStatus + "\"}")
	        .contentType(ContentType.JSON)
	    .when()
         	.put("/api/v3/books/{bookId}/change-status", newBookId)
        .then()
	        .statusCode(400)
	        .body("message", equalTo("JSON parse error: Cannot deserialize value of type `com.poletto.bookstore.entities.enums.BookStatus` from String \"" + invalidStatus + "\":"
	        					   + " not one of the values accepted for Enum class: [INACTIVE, ACTIVE]"));
    	
    	logger.info("test success, when the user requested to change a book status with a existing book id and invalid status, the response properly provided the exception with message explaining");
    	
    }
    
    private void testCreateBookWithInvalidData(Map<String, Object> invalidDataMap, String expectedErrorMessage) {
    	
    	given()
	        .spec(UserAuthMocks.OperatorPrivilegesUser())
	        .body(invalidDataMap)
	        .contentType(ContentType.JSON)
	    .when()
        	.post("/api/v3/books/create")
        .then()
	        .statusCode(422)
	        .body("message", equalTo(expectedErrorMessage));
    }

    private void testUpdateBookWithInvalidData(Long bookId, Map<String, Object> invalidDataMap, String expectedErrorMessage) {
	
		given()
	        .spec(UserAuthMocks.OperatorPrivilegesUser())
	        .body(invalidDataMap)
	        .contentType(ContentType.JSON)
	    .when()
	    	.put("/api/v3/books/{bookId}/update", bookId)
	    .then()
	        .statusCode(422)
	        .body("message", equalTo(expectedErrorMessage));
    }
}
