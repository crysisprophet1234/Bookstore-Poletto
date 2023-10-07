package com.poletto.bookstore.services.v3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.poletto.bookstore.dto.v2.BookDTOv2;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.repositories.v2.BookRepository;
import com.poletto.bookstore.services.v3.mocks.BookMocks;
import com.poletto.bookstore.util.CustomRedisClient;

import redis.embedded.RedisServer;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(Lifecycle.PER_CLASS)
public class BookServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(BookServiceTest.class);

	@Autowired
	private CustomRedisClient<String , BookDTOv2> client;

	private static RedisServer redisServer;

	@SpyBean
	private BookRepository repository;

	@Autowired
	@InjectMocks
	private BookService service;

	BookDTOv2 dto;
	BookDTOv2 insertDto;

	@BeforeAll
	public void startRedis() {

		try {
			redisServer = RedisServer.builder().port(6370).setting("maxmemory 128M").build();
			redisServer.start();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	@BeforeEach
	public void setUp() {

		insertDto = BookMocks.insertBookMockDto();
		
		dto = BookMocks.bookMockDto(1L);

		service.insert(insertDto);

	}

	@AfterEach
	public void tearDown() {

		dto = insertDto = null;

		client.flushDb();

	}

	@AfterAll
	public void cleanUp() {
		redisServer.stop();
	}

	@Test
	void isCacheBeingSavedAfterQueryById() {

		logger.info("\n\n<=========  STARTING TEST isCacheBeingSavedAfterQueryById()  =========>\n");

		logger.info("querying book with id 1 from the repository");

		BookDTOv2 dto1 = service.findById(1L);

		assertNotNull(dto1, "book with id 1 not found on db");

		logger.info("dto retrieved from db: {}", dto1);

		logger.info("querying book by id 1 again to assert that the cache will get hit");

		BookDTOv2 cachedDto1 = (BookDTOv2) client.get("book::1");

		assertNotNull(cachedDto1, "book with id 1 not found on cache");

		logger.info("dto retrieved from cache: {}", cachedDto1);

		logger.info("asserting that dto from db and cache are equals");

		assertEquals(dto1, cachedDto1, "dto from db and cache were not equals");

		logger.info("querying book with id 1 again to assert that the cache will get hit");

		service.findById(1L);

		logger.info("asserting that the repository will not get a call");

		verify(repository, times(1)).findById(1L);

		logger.info("test sucess, service called two times and repository only one");

	}

	@Test
	void isCacheGettingUpdatedAfterUpdateAndDelete() {

		logger.info("\n\n<=========  STARTING TEST isCacheGettingUpdatedAfterBookUpdate()  =========>\n");

		logger.info("querying book with id 1 from the repository");

		BookDTOv2 dto1 = service.findById(1L);

		assertNotNull(dto1, "book with id 1 not found on db");

		logger.info("dto retrieved from db: {}", dto1);

		logger.info("querying book by id 1 again to assert that the cache will get hit");

		BookDTOv2 cachedDto1 = (BookDTOv2) client.get("book::1");

		assertNotNull(cachedDto1, "book with id 1 not found on cache");

		logger.info("dto retrieved from cache: {}", cachedDto1);

		logger.info("asserting that dto from db and cache are equals");

		assertEquals(dto1, cachedDto1, "dto from db and cache were not equals");

		logger.info("updating book name with id 1 to check if cache gets updated");

		dto1.setName("Book new name");

		dto1 = service.update(1L, dto1);

		cachedDto1 = (BookDTOv2) client.get("book::1");

		assertNotNull(cachedDto1, "book with id 1 not found on cache");

		logger.info("asserting that the cache got updated");

		assertEquals(dto1, cachedDto1, "cache of book with id 1 didnt got updated");

		logger.info("deleting book with id 1 from the db");

		service.delete(1L);

		logger.info("asserting that the book 1 cache got evicted");

		cachedDto1 = (BookDTOv2) client.get("book::1");

		assertNull(cachedDto1, "cache of book 1 didnt got evicted");

		logger.info("calling find by id to check if the repository gets called and throws not found exception");

		assertThrows(ResourceNotFoundException.class, () -> service.findById(1L),
				"service actually found book with id 1");

		verify(repository, times(2)).findById(1L);

		logger.info("test sucess, service called two times and repository only one");

	}

	@Test
	void isCacheGettingEvictedAfterInsertsAndUpdates() {

		logger.info("\n\n<=========  STARTING TEST isCacheGettingEvictedAfterInsertsAndUpdates()  =========>\n");

		logger.info("querying page of books on the repository");

		Page<BookDTOv2> dtoPage = findPageOfBooksFromService();

		assertNotNull(dtoPage, "dtoPage not found on repository");

		assertNotEquals(List.of().size(), dtoPage.getContent().size(), "dtoPage found but with 0 elements");

		logger.info("dtoPage retrieved from db: {}", dtoPage.getContent());

		logger.info("querying page of books on the cache");

		Page<BookDTOv2> cachedDtoPage = findPageOfBooksFromCache();

		assertNotNull(cachedDtoPage, "dtoPage with not found on cache");

		assertNotEquals(List.of().size(), cachedDtoPage.getContent().size(), "cacheDtoPage found but with 0 elements");
		
		logger.info("dtoPage retrieved from cache: {}", cachedDtoPage.getContent());
		
		assertEquals(
				dtoPage.getContent(),
				cachedDtoPage.getContent(),
				"dtoPage from db and cache are not equals"
		);
		
		logger.info("querying dtoPage from repo again to assert that the cache will get hit");
		
		findPageOfBooksFromService();
		
		logger.info("asserting that the repository will not get a call");
		
		verify(repository, times(1)).findPaged(any(), any(), any(), any());
		
		logger.info("inserting new book to check if the page cache gets updated");
		
		service.insert(insertDto);
		
		logger.info("querying dtoPage again to assert that the repo will get called one more time");
		
		dtoPage = findPageOfBooksFromService();
		
		verify(repository, times(2)).findPaged(any(), any(), any(), any());
		
		logger.info("asserting that the new dtoPage queried is different from the previous cache");
		
		assertNotEquals(
				dtoPage.getContent(),
				cachedDtoPage.getContent(),
				"dtoPage from db and cache are equals"
		);
		
		logger.info("asserting that the new dtoPage queried is equals to the new cache");
		
		cachedDtoPage = findPageOfBooksFromCache();
		
		assertEquals(
				dtoPage.getContent(),
				cachedDtoPage.getContent(),
				"dtoPage from db and cache are not equals"
		);
		
		logger.info("deleting book with id 1 to check if the page cache gets updated");
		
		service.delete(1L);
		
		logger.info("querying dtoPage again to assert that the repo will get called one more time");
		
		dtoPage = findPageOfBooksFromService();
		
		verify(repository, times(3)).findPaged(any(), any(), any(), any());
		
		logger.info("asserting that the new dtoPage queried is different from the previous cache");
		
		assertNotEquals(
				dtoPage.getContent(),
				cachedDtoPage.getContent(),
				"dtoPage from db and cache are equals"
		);
		
		logger.info("asserting that the new dtoPage queried is equals to the new cache");
		
		cachedDtoPage = findPageOfBooksFromCache();
		
		assertEquals(
				dtoPage.getContent(),
				cachedDtoPage.getContent(),
				"dtoPage from db and cache are not equals"
		);
		
		logger.info("querying dtoPage from repo again to assert that the cache will get hit");
		
		findPageOfBooksFromService();
		
		verify(repository, times(3)).findPaged(any(), any(), any(), any());
		
		logger.info("test sucess, service find by id called 5 times and repository only 3");

	}

	@Test
	void isCacheGettingProperlySettedUsingClient() {
		
		logger.info("\n\n<=========  STARTING TEST isCacheGettingProperlySettedUsingClient()  =========>\n");
		
		String bookKey = "book#1";
		
		logger.info("setting on the cache value [{}] with key [{}]", dto, bookKey);
		
		assertTrue(client.set(bookKey, dto), "cache didnt got setted");
		
		logger.info("asserting that the cache value is not null");
		
		assertNotNull(client.get(bookKey));
		
		logger.info("asserting that the cache value is equals to bookDto");
		
		assertEquals(dto, client.get(bookKey), "cache value didnt get setted properly");
		
		logger.info("updating value on the cache");
		
		dto.setName("new book name");
		
		assertTrue(client.put(bookKey, dto), "cache value didnt get updated");
		
		logger.info("asserting that the value got updated");
		
		assertEquals(dto.getName(), client.get(bookKey).getName());
		
		logger.info("test success, cache got setted, retrieved and updated properly");
		
	}
	
	private Page<BookDTOv2> findPageOfBooksFromService() {

		Pageable pageable = PageRequest.of(0, 12);

		return service.findAllPaged(pageable, null, "", "all");

	}

	private Page<BookDTOv2> findPageOfBooksFromCache() {

		String expectedCacheKey = "books::SimpleKey [Page request [number: 0, size 12, sort: UNSORTED],null,,all]";

		return ((Page<?>) client.get(expectedCacheKey)).map(x -> (BookDTOv2) x);

	}

}
