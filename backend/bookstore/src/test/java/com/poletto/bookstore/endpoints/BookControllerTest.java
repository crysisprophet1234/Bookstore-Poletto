package com.poletto.bookstore.endpoints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.poletto.bookstore.config.JwtService;
import com.poletto.bookstore.controllers.BookController;
import com.poletto.bookstore.dto.v1.BookDTO;
import com.poletto.bookstore.entities.Author;
import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.enums.BookStatus;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.mocks.MockBook;
import com.poletto.bookstore.repositories.BookRepository;
import com.poletto.bookstore.services.BookService;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

	private Logger logger = Logger.getLogger(BookControllerTest.class.getName());

	@MockBean
	private JwtService jwtService;

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private BookRepository bookRepository;

	@MockBean
	private BookService bookService;

	@InjectMocks
	MockBook inputObject;

	@Before
	public void setUp() {
		inputObject = new MockBook();
	}

	@SuppressWarnings("deprecation")
	@Test
	@WithMockUser(username = "leo@gmail.com", roles = { "ADMIN", "OPERATOR" })
	public void testFindAll() throws Exception {

		List<Book> books = inputObject.mockEntityList();

		when(bookRepository.findAll()).thenReturn(books);

		var result = bookService.findAll();

		for (Integer i = 0; i < result.size(); i++) {

			logger.info(result.get(i).toString());

			mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books").contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(15)))

					.andExpect(MockMvcResultMatchers.jsonPath("$[" + i.longValue() + "].id", Matchers.is(i)))
					.andExpect(MockMvcResultMatchers.jsonPath("$[" + i.longValue() + "].name",
							Matchers.is("Book Test " + i)))
					.andExpect(MockMvcResultMatchers.jsonPath("$[" + i.longValue() + "].releaseDate",
							Matchers.is(LocalDate.now().minusWeeks(i).toString())))
					.andExpect(MockMvcResultMatchers.jsonPath("$[" + i.longValue() + "].imgUrl",
							Matchers.is("Img Url Test " + i)))
					.andExpect(MockMvcResultMatchers.jsonPath("$[" + i.longValue() + "].status",
							Matchers.is((i & 1) == 0 ? BookStatus.BOOKED.toString() : BookStatus.AVAILABLE.toString())))
					.andExpect(MockMvcResultMatchers.jsonPath("$[" + i.longValue() + "].author.id", Matchers.is(i)))
					.andExpect(MockMvcResultMatchers.jsonPath("$[" + i.longValue() + "].author.name",
							Matchers.is("Author Name Test " + i)))
					.andExpect(MockMvcResultMatchers.jsonPath("$[" + i.longValue() + "].categories.[0].id",
							Matchers.is(i)))
					.andExpect(MockMvcResultMatchers.jsonPath("$[" + i.longValue() + "].categories.[0].name",
							Matchers.is("Category Test " + i)));

		}

	}

	@Test
	@WithMockUser(username = "leo@gmail.com", roles = { "ADMIN", "OPERATOR" })
	public void testFindById() throws Exception {

		Long id = 1L;

		Book entity = inputObject.mockEntity();

		Optional<Book> book = Optional.of(entity);

		when(bookRepository.findById(id)).thenReturn(book);

		// var result = bookService.findById(id);
		
		entity = bookRepository.findById(id).orElseThrow();

		assertNotNull(entity);
		assertNotNull(entity.getId());
		assertEquals(entity.getName(), "Book Test 0");
		assertEquals(entity.getImgUrl(), "Img Url Test 0");
		assertEquals(entity.getStatus(), BookStatus.BOOKED);
		assertEquals(entity.getReleaseDate(), LocalDate.now().minusWeeks(0));
		assertEquals(entity.getAuthor(), new Author(0L, "Author Name Test 0", "terrestre"));

	}

	@Test
	@WithMockUser(username = "leo@gmail.com", roles = { "ADMIN", "OPERATOR" })
	public void testFindById_ResourceNotFoundException() {

		Long id = 1L;
		Optional<Book> optionalBook = Optional.empty();

		when(bookRepository.findById(id)).thenReturn(optionalBook);

		try {
			bookService.findById(id);

		} catch (ResourceNotFoundException e) {

			assertEquals("Book not found for id: " + id, e.getMessage());

		}
	}

	@Test
	@WithMockUser(username = "leo@gmail.com", roles = { "ADMIN", "OPERATOR" })
	public void insertTest() throws Exception {
		
		Book entity = inputObject.mockEntity(1);

		Book persisted = entity;
		persisted.setId(1L);

		BookDTO bookDTO = inputObject.mockDTO(1);
		bookDTO.setId(1L);

		when(bookRepository.save(entity)).thenReturn(persisted);

		var result = bookService.insert(bookDTO);
		
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(entity.getName(), "Book Test 0");
		assertEquals(entity.getImgUrl(), "Img Url Test 0");
		assertEquals(entity.getStatus(), BookStatus.BOOKED);
		assertEquals(entity.getReleaseDate(), LocalDate.now().minusWeeks(0));
		assertEquals(entity.getAuthor(), new Author(0L, "Author Name Test 0", "terrestre"));
		
	}
	
}