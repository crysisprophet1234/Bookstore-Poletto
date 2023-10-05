package com.poletto.bookstore.services.v3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.poletto.bookstore.dto.v2.BookDTOv2;
import com.poletto.bookstore.repositories.v2.BookRepository;
import com.poletto.bookstore.services.v3.mocks.BookMocks;

import redis.embedded.RedisServer;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(Lifecycle.PER_CLASS)
public class BookServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(BookServiceTest.class);

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

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

		insertDto = BookMocks.insertBookMock();

		insertDto = service.insert(insertDto);

	}

	@AfterEach
	public void tearDown() {
		
		dto = insertDto = null;
		
		redisTemplate.execute((RedisCallback<Object>) connection -> {
			connection.serverCommands().flushAll();
			return null;
		});
		
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

		BookDTOv2 cachedDto1 = (BookDTOv2) redisTemplate.opsForValue().get("book::1");

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

}
