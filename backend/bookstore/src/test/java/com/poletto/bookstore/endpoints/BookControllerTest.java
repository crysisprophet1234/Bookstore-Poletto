package com.poletto.bookstore.endpoints;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
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
import com.poletto.bookstore.dto.BookDTO;
import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.enums.BookStatus;
import com.poletto.bookstore.mocks.MockBook;
import com.poletto.bookstore.services.BookService;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

	@MockBean
	private JwtService jwtService;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookService bookService;

	@InjectMocks
	MockBook inputObject;

	@Before
	public void setUp() {
		inputObject = new MockBook();
	}

	@Test
	@WithMockUser(username = "leo@gmail.com", roles = { "ADMIN", "OPERATOR" })
	public void testFindAll() throws Exception {

		AtomicLong aLong = new AtomicLong(0);
		AtomicInteger aInt = new AtomicInteger(0);

		List<Book> books = inputObject.mockEntityList();

		Mockito.when(bookService.findAll())
				.thenReturn(books.stream().map(x -> new BookDTO(x)).collect(Collectors.toList()));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(15)))

				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].name",
						Matchers.is("Book Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].releaseDate",
						Matchers.is(LocalDate.now().minusWeeks(aInt.get()).toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].imgUrl",
						Matchers.is("Img Url Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].status",
						Matchers.is((aInt.get() & 1) == 0 ? BookStatus.BOOKED.toString()
								: BookStatus.AVAILABLE.toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.name",
						Matchers.is("Author Name Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].categories.[0].id",
						Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.getAndIncrement() + "].categories.[0].name",
						Matchers.is("Category Test " + aInt.getAndIncrement())))
				
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].name",
						Matchers.is("Book Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].releaseDate",
						Matchers.is(LocalDate.now().minusWeeks(aInt.get()).toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].imgUrl",
						Matchers.is("Img Url Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].status",
						Matchers.is((aInt.get() & 1) == 0 ? BookStatus.BOOKED.toString()
								: BookStatus.AVAILABLE.toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.name",
						Matchers.is("Author Name Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].categories.[0].id",
						Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.getAndIncrement() + "].categories.[0].name",
						Matchers.is("Category Test " + aInt.getAndIncrement())))
				
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].name",
						Matchers.is("Book Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].releaseDate",
						Matchers.is(LocalDate.now().minusWeeks(aInt.get()).toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].imgUrl",
						Matchers.is("Img Url Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].status",
						Matchers.is((aInt.get() & 1) == 0 ? BookStatus.BOOKED.toString()
								: BookStatus.AVAILABLE.toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.name",
						Matchers.is("Author Name Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].categories.[0].id",
						Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.getAndIncrement() + "].categories.[0].name",
						Matchers.is("Category Test " + aInt.getAndIncrement())))
				
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].name",
						Matchers.is("Book Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].releaseDate",
						Matchers.is(LocalDate.now().minusWeeks(aInt.get()).toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].imgUrl",
						Matchers.is("Img Url Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].status",
						Matchers.is((aInt.get() & 1) == 0 ? BookStatus.BOOKED.toString()
								: BookStatus.AVAILABLE.toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.name",
						Matchers.is("Author Name Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].categories.[0].id",
						Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.getAndIncrement() + "].categories.[0].name",
						Matchers.is("Category Test " + aInt.getAndIncrement())));

	}

	@Test
	@WithMockUser(username = "leo@gmail.com", roles = { "ADMIN", "OPERATOR" })
	public void testFindAllDTO() throws Exception {

		AtomicLong aLong = new AtomicLong(0);
		AtomicInteger aInt = new AtomicInteger(0);

		List<BookDTO> books = inputObject.mockBookDTO();

		Mockito.when(bookService.findAll()).thenReturn(books);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(15)))

				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].name",
						Matchers.is("Book Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].releaseDate",
						Matchers.is(LocalDate.now().minusWeeks(aInt.get()).toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].imgUrl",
						Matchers.is("Img Url Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].status",
						Matchers.is((aInt.get() & 1) == 0 ? BookStatus.BOOKED.toString()
								: BookStatus.AVAILABLE.toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.name",
						Matchers.is("Author Name Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].categories.[0].id",
						Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.getAndIncrement() + "].categories.[0].name",
						Matchers.is("Category Test " + aInt.getAndIncrement())))

				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].name",
						Matchers.is("Book Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].releaseDate",
						Matchers.is(LocalDate.now().minusWeeks(aInt.get()).toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].imgUrl",
						Matchers.is("Img Url Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].status",
						Matchers.is((aInt.get() & 1) == 0 ? BookStatus.BOOKED.toString()
								: BookStatus.AVAILABLE.toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.name",
						Matchers.is("Author Name Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].categories.[0].id",
						Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.getAndIncrement() + "].categories.[0].name",
						Matchers.is("Category Test " + aInt.getAndIncrement())))

				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].name",
						Matchers.is("Book Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].releaseDate",
						Matchers.is(LocalDate.now().minusWeeks(aInt.get()).toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].imgUrl",
						Matchers.is("Img Url Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].status",
						Matchers.is((aInt.get() & 1) == 0 ? BookStatus.BOOKED.toString()
								: BookStatus.AVAILABLE.toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.name",
						Matchers.is("Author Name Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].categories.[0].id",
						Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.getAndIncrement() + "].categories.[0].name",
						Matchers.is("Category Test " + aInt.getAndIncrement())))

				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].name",
						Matchers.is("Book Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].releaseDate",
						Matchers.is(LocalDate.now().minusWeeks(aInt.get()).toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].imgUrl",
						Matchers.is("Img Url Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].status",
						Matchers.is((aInt.get() & 1) == 0 ? BookStatus.BOOKED.toString()
								: BookStatus.AVAILABLE.toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.name",
						Matchers.is("Author Name Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].categories.[0].id",
						Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.getAndIncrement() + "].categories.[0].name",
						Matchers.is("Category Test " + aInt.getAndIncrement())))

				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].name",
						Matchers.is("Book Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].releaseDate",
						Matchers.is(LocalDate.now().minusWeeks(aInt.get()).toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].imgUrl",
						Matchers.is("Img Url Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].status",
						Matchers.is((aInt.get() & 1) == 0 ? BookStatus.BOOKED.toString()
								: BookStatus.AVAILABLE.toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.name",
						Matchers.is("Author Name Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].categories.[0].id",
						Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.getAndIncrement() + "].categories.[0].name",
						Matchers.is("Category Test " + aInt.getAndIncrement())))

				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].name",
						Matchers.is("Book Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].releaseDate",
						Matchers.is(LocalDate.now().minusWeeks(aInt.get()).toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].imgUrl",
						Matchers.is("Img Url Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].status",
						Matchers.is((aInt.get() & 1) == 0 ? BookStatus.BOOKED.toString()
								: BookStatus.AVAILABLE.toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.name",
						Matchers.is("Author Name Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].categories.[0].id",
						Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.getAndIncrement() + "].categories.[0].name",
						Matchers.is("Category Test " + aInt.getAndIncrement())))

				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].name",
						Matchers.is("Book Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].releaseDate",
						Matchers.is(LocalDate.now().minusWeeks(aInt.get()).toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].imgUrl",
						Matchers.is("Img Url Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].status",
						Matchers.is((aInt.get() & 1) == 0 ? BookStatus.BOOKED.toString()
								: BookStatus.AVAILABLE.toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.name",
						Matchers.is("Author Name Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].categories.[0].id",
						Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.getAndIncrement() + "].categories.[0].name",
						Matchers.is("Category Test " + aInt.getAndIncrement())))

				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].name",
						Matchers.is("Book Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].releaseDate",
						Matchers.is(LocalDate.now().minusWeeks(aInt.get()).toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].imgUrl",
						Matchers.is("Img Url Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].status",
						Matchers.is((aInt.get() & 1) == 0 ? BookStatus.BOOKED.toString()
								: BookStatus.AVAILABLE.toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.name",
						Matchers.is("Author Name Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].categories.[0].id",
						Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.getAndIncrement() + "].categories.[0].name",
						Matchers.is("Category Test " + aInt.getAndIncrement())))

				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].name",
						Matchers.is("Book Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].releaseDate",
						Matchers.is(LocalDate.now().minusWeeks(aInt.get()).toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].imgUrl",
						Matchers.is("Img Url Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].status",
						Matchers.is((aInt.get() & 1) == 0 ? BookStatus.BOOKED.toString()
								: BookStatus.AVAILABLE.toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.name",
						Matchers.is("Author Name Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].categories.[0].id",
						Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.getAndIncrement() + "].categories.[0].name",
						Matchers.is("Category Test " + aInt.getAndIncrement())))

				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].name",
						Matchers.is("Book Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].releaseDate",
						Matchers.is(LocalDate.now().minusWeeks(aInt.get()).toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].imgUrl",
						Matchers.is("Img Url Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].status",
						Matchers.is((aInt.get() & 1) == 0 ? BookStatus.BOOKED.toString()
								: BookStatus.AVAILABLE.toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.id", Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].author.name",
						Matchers.is("Author Name Test " + aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.get() + "].categories.[0].id",
						Matchers.is(aInt.get())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[" + aLong.getAndIncrement() + "].categories.[0].name",
						Matchers.is("Category Test " + aInt.getAndIncrement())));

	}

}