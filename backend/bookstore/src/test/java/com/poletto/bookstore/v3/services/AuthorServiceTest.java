package com.poletto.bookstore.v3.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.poletto.bookstore.converter.DozerMapperConverter;
import com.poletto.bookstore.dto.v2.AuthorDTOv2;
import com.poletto.bookstore.entities.Author;
import com.poletto.bookstore.repositories.v3.AuthorRepository;
import com.poletto.bookstore.services.v3.AuthorService;
import com.poletto.bookstore.util.CustomRedisClient;

import redis.embedded.RedisServer;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(Lifecycle.PER_CLASS)
public class AuthorServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthorServiceTest.class);
	
	@Autowired
	private CustomRedisClient<String , Author> client;

	private static RedisServer redisServer;

	@SpyBean
	private AuthorRepository repository;

	@Autowired
	@InjectMocks
	private AuthorService service;
	
	AuthorDTOv2 authorDto;
	
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
		authorDto = new AuthorDTOv2();
	}
	
	@AfterAll
	public void cleanUp() {
		authorDto = null;
		redisServer.stop();
	}
	
	@Test
	void isAuthorBeingCachedAfterQueryById() {
		
		logger.info("\n\n<=========  STARTING TEST isAuthorBeingCachedAfterQueryById()  =========>\n");
		
		logger.info("querying author with id 1 on the repository");

		assertDoesNotThrow(() -> authorDto = service.findById(1L),
				"error thrown when trying to query author with id 1");

		logger.info("querying author with id 1 on the cache");

		AuthorDTOv2 cachedAuthorDto = DozerMapperConverter.parseObject(client.get("author::" + 1L), AuthorDTOv2.class);

		assertNotNull(cachedAuthorDto, "cachedAuthorDto was null");

		logger.info("asserting that authorDto is equals to cachedAuthorDto");

		assertEquals(authorDto, cachedAuthorDto, "authorDto and cachedAuthorDto were not equals");

		logger.info("querying author with id 1 on the repository again to check if cache gets hit");

		assertDoesNotThrow(() -> authorDto = service.findById(1L),
				"error thrown when trying to query author with id 1");

		logger.info("asserting that new authorDto is equals to cachedAuthorDto");

		assertEquals(authorDto, cachedAuthorDto, "new authorDto and cachedAuthorDto were not equals");

		logger.info("asserting that repository was invoked only once");

		verify(repository, times(1)).findById(1L);

		logger.info("test success, author with id 1 got properly cached and repository was only invoked one time");	
		
	}
	
	@Test
	void isAuthorListBeingCachedAfterQueryAll() {

		logger.info("\n\n<=========  STARTING TEST isAuthorListBeingCachedAfterQueryAll()  =========>\n");
		
		String expectedCacheKey = "authorsList::SimpleKey []";

		logger.info("querying author list from repository");

		List<AuthorDTOv2> authorList = service.findAll();

		assertNotNull(authorList, "authorList was null");

		logger.info("querying author list from cache");

		List<AuthorDTOv2> cachedAuthorList = ((List<?>) client.get(expectedCacheKey))
				.stream()
				.map(x -> DozerMapperConverter.parseObject(x, AuthorDTOv2.class))
				.collect(Collectors.toList());

		assertNotNull(cachedAuthorList, "cachedAuthorList was null");

		logger.info("asserting authorList from repository and cache are equals");

		assertEquals(authorList, cachedAuthorList, "authorList from repository and cache were not equals");

		logger.info("querying author list on the repository again to check if cache gets hit");

		authorList = service.findAll();

		logger.info("asserting that the updated authorList is equals to cachedAuthorList");

		assertEquals(authorList, cachedAuthorList, "updated authorList and cachedAuthorList were not equals");

		logger.info("asserting that repository was invoked only once");

		verify(repository, times(1)).findAll();

		logger.info("test success, authorList got properly cached and repository was only invoked one time");

	}
	

}
