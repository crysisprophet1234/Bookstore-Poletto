package com.poletto.bookstore.v3.services.caching;

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
import com.poletto.bookstore.dto.v2.CategoryDTOv2;
import com.poletto.bookstore.entities.Category;
import com.poletto.bookstore.repositories.v3.CategoryRepository;
import com.poletto.bookstore.services.v3.CategoryService;
import com.poletto.bookstore.util.CustomRedisClient;

import redis.embedded.RedisServer;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(Lifecycle.PER_CLASS)
public class CategoryServiceCacheTest {

private static final Logger logger = LoggerFactory.getLogger(AuthorServiceCacheTest.class);
	
	@Autowired
	private CustomRedisClient<String , Category> client;

	private static RedisServer redisServer;

	@SpyBean
	private CategoryRepository repository;

	@Autowired
	@InjectMocks
	private CategoryService service;
	
	CategoryDTOv2 categoryDto;
	
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
		categoryDto = new CategoryDTOv2();
	}
	
	@AfterAll
	public void cleanUp() {
		categoryDto = null;
		redisServer.stop();
	}
	
	@Test
	void isCategoryBeingCachedAfterQueryById() {
		
		logger.info("\n\n<=========  STARTING TEST isCategoryBeingCachedAfterQueryById()  =========>\n");
		
		logger.info("querying category with id 1 on the repository");

		assertDoesNotThrow(() -> categoryDto = service.findById(1L), "error thrown when trying to query category with id 1");

		logger.info("querying category with id 1 on the cache");

		CategoryDTOv2 cachedCategoryDto = DozerMapperConverter.parseObject(client.get("category::" + 1L), CategoryDTOv2.class);

		assertNotNull(cachedCategoryDto, "cachedCategoryDto was null");

		logger.info("asserting that categoryDto is equals to cachedCategoryDto");

		assertEquals(categoryDto, cachedCategoryDto, "categoryDto and cachedCategoryDto were not equals");

		logger.info("querying category with id 1 on the repository again to check if cache gets hit");

		assertDoesNotThrow(() -> categoryDto = service.findById(1L), "error thrown when trying to query category with id 1");

		logger.info("asserting that new categoryDto is equals to cachedCategoryDto");

		assertEquals(categoryDto, cachedCategoryDto, "new categoryDto and cachedCategoryDto were not equals");

		logger.info("asserting that repository was invoked only once");

		verify(repository, times(1)).findById(1L);

		logger.info("test success, category with id 1 got properly cached and repository was only invoked one time");	
		
	}
	
	@Test
	void isCategoryListBeingCachedAfterQueryAll() {

		logger.info("\n\n<=========  STARTING TEST isCategoryListBeingCachedAfterQueryAll()  =========>\n");
		
		String expectedCacheKey = "categoriesList::SimpleKey []";

		logger.info("querying category list from repository");

		List<CategoryDTOv2> categoryList = service.findAll();

		assertNotNull(categoryList, "categoryList was null");

		logger.info("querying category list from cache");

		List<CategoryDTOv2> cachedCategoryList = ((List<?>) client.get(expectedCacheKey))
				.stream()
				.map(x -> DozerMapperConverter.parseObject(x, CategoryDTOv2.class))
				.collect(Collectors.toList());

		assertNotNull(cachedCategoryList, "cachedCategoryList was null");

		logger.info("asserting categoryList from repository and cache are equals");

		assertEquals(categoryList, cachedCategoryList, "categoryList from repository and cache were not equals");

		logger.info("querying category list on the repository again to check if cache gets hit");

		categoryList = service.findAll();

		logger.info("asserting that the updated categoryList is equals to cachedCategoryList");

		assertEquals(categoryList, cachedCategoryList, "updated categoryList and cachedCategoryList were not equals");

		logger.info("asserting that repository was invoked only once");

		verify(repository, times(1)).findAll();

		logger.info("test success, categoryList got properly cached and repository was only invoked one time");

	}
	
	
}
