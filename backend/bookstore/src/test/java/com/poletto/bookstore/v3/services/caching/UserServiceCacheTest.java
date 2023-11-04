package com.poletto.bookstore.v3.services.caching;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.poletto.bookstore.converter.custom.UserMapper;
import com.poletto.bookstore.dto.v2.UserAuthDTOv2;
import com.poletto.bookstore.dto.v2.UserDTOv2;
import com.poletto.bookstore.entities.User;
import com.poletto.bookstore.repositories.v3.UserRepository;
import com.poletto.bookstore.services.v3.AuthService;
import com.poletto.bookstore.services.v3.UserService;
import com.poletto.bookstore.util.CustomRedisClient;
import com.poletto.bookstore.v3.mocks.UserMocks;

import redis.embedded.RedisServer;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(Lifecycle.PER_CLASS)
public class UserServiceCacheTest {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceCacheTest.class);

	@Autowired
	private CustomRedisClient<String, User> client;

	private static RedisServer redisServer;

	@SpyBean
	private UserRepository userRepository;

	@Autowired
	@InjectMocks
	private UserService userService;

	@Autowired
	@InjectMocks
	private AuthService authService;

	UserDTOv2 userDto;
	UserAuthDTOv2 insertUserDto;

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

		userDto = UserMocks.userMockDto();
		insertUserDto = UserMocks.registerUserMockDto();

	}

	@AfterEach
	public void tearDown() {

		userDto = insertUserDto = null;

		client.clear();

	}

	@AfterAll
	public void cleanUp() {
		redisServer.stop();
	}

	@Test
	void isUserBeingCachedAfterQueryById() {

		logger.info("\n\n<=========  STARTING TEST isUserBeingCachedAfterQueryById()  =========>\n");

//		logger.info("inserting a new user");
//		
//		assertDoesNotThrow(() -> userDto = authService.register(insertUserDto), "error thrown when trying to insert new user");

		logger.info("querying user with id 1 on the repository");

		assertDoesNotThrow(() -> userDto = userService.findById(1L), "error thrown when trying to query user with id 1");

		logger.info("querying user with id 1 on the cache");

		UserDTOv2 cachedUserDto = UserMapper.convertEntityToDtoV2((User) client.get("user::" + 1L));

		assertNotNull(cachedUserDto, "cachedUserDto was null");

		logger.info("asserting that userDto is equals to cachedUserDto");

		assertEquals(userDto, cachedUserDto, "userDto and cachedUserDto were not equals");

		logger.info("querying user with id 1 on the repository again to check if cache gets hit");

		assertDoesNotThrow(() -> userDto = userService.findById(1L), "error thrown when trying to query user with id 1");

		logger.info("asserting that new userDto is equals to cachedUserDto");

		assertEquals(userDto, cachedUserDto, "new userDto and cachedUserDto were not equals");

		logger.info("asserting that repository was invoked only once");

		verify(userRepository, times(1)).findById(1L);

		logger.info("test success, user with id 1 got properly cached and repository was only invoked one time");

	}

	@Test
	void isUserPageBeingCachedAfterPagedQuery() {

		logger.info("\n\n<=========  STARTING TEST isUserPageBeingCachedAfterPagedQuery()  =========>\n");

		logger.info("querying user page from repository");

		Page<UserDTOv2> userPage = findPageOfUsersFromService();

		assertNotNull(userPage, "userPage was null");

		logger.info("querying user page from cache");

		Page<UserDTOv2> cachedUserPage = findPageOfUsersFromCache();

		assertNotNull(cachedUserPage, "cachedUserPage was null");

		logger.info("asserting userPage from repository and cache are equals");

		assertEquals(userPage.getContent(), cachedUserPage.getContent(), "userPage from repository and cache were not equals");

		logger.info("querying user page on the repository again to check if cache gets hit");

		userPage = findPageOfUsersFromService();

		logger.info("asserting that the updated userPage is equals to cachedUserPage");

		assertEquals(userPage, cachedUserPage, "updated userPage and cachedUserPage were not equals");

		logger.info("asserting that repository was invoked only once");

		verify(userRepository, times(1)).findAll(any(Pageable.class));

		logger.info("test success, userPage got properly cached and repository was only invoked one time");

	}

	@Test
	void isUserCacheBeingUpdated() {

		logger.info("\n\n<=========  STARTING TEST isUserCacheBeingUpdated()  =========>\n");

		Long userId = 1L;

		logger.info("querying user with {} on the repository", userId);

		assertDoesNotThrow(() -> userDto = userService.findById(userId), "error thrown when trying to query user with id " + userId);

		logger.info("querying user with id {} on the cache", userId);

		UserDTOv2 cachedUserDto = UserMapper.convertEntityToDtoV2((User) client.get("user::" + userId));

		assertNotNull(cachedUserDto, "cachedUserDto was null");

		logger.info("asserting that userDto is equals to cachedUserDto");

		assertEquals(userDto, cachedUserDto, "userDto and cachedUserDto were not equals");

		logger.info("updating user with id {}", userId);

		UserAuthDTOv2 updateUserDto = UserMocks.userAuthDto(userId);

		updateUserDto.setFirstname("Newname");

		assertDoesNotThrow(() -> userDto = userService.update(userId, updateUserDto), "error thrown when trying to update user with id " + userId);

		logger.info("asserting that updated userDto is not equals to previous cached userDto");

		assertNotEquals(userDto, cachedUserDto, "updated userDto was equals to previous cached userDto");

		logger.info("querying user with id {} again", userId);

		assertDoesNotThrow(() -> userDto = userService.findById(userId), "error thrown when trying to query user with id " + userId);

		logger.info("querying user with id {} on the cache again", userId);

		cachedUserDto = UserMapper.convertEntityToDtoV2((User) client.get("user::" + userId));

		logger.info("asserting that the cache got properly updated");

		assertEquals(userDto, cachedUserDto, "updated userDto was not equals to updated cached userDto");

		logger.info("asserting that the repository was invoked only once");

		verify(userRepository, times(1)).findById(userId);

		logger.info("test success, user with id {} got properly cached and updated, repository was only invoked one time", userId);

	}

	@Test
	void isUserPageAndSingleCacheGettingEvictedAfterDeletion() {

		logger.info("\n\n<=========  STARTING TEST isUserPageAndSingleCacheGettingEvictedAfterDeletion()  =========>\n");

		logger.info("querying user page from repository");

		Page<UserDTOv2> userPage = findPageOfUsersFromService();

		assertNotNull(userPage, "userPage was null");

		logger.info("querying user page from cache");

		Page<UserDTOv2> cachedUserPage = findPageOfUsersFromCache();

		assertNotNull(cachedUserPage, "cachedUserPage was null");

		logger.info("asserting userPage from repository and cache are equals");

		assertEquals(userPage.getContent(), cachedUserPage.getContent(),
				"userPage from repository and cache were not equals");

		logger.info("querying user with id 1 on the repository");

		assertDoesNotThrow(() -> userDto = userService.findById(1L),
				"error thrown when trying to query user with id 1");

		logger.info("querying user with id 1 on the cache");

		UserDTOv2 cachedUserDto = UserMapper.convertEntityToDtoV2((User) client.get("user::" + 1L));

		assertNotNull(cachedUserDto, "cachedUserDto was null");

		logger.info("asserting that userDto is equals to cachedUserDto");

		assertEquals(userDto, cachedUserDto, "userDto and cachedUserDto were not equals");

		logger.info("deleting user with id 1");

		assertDoesNotThrow(() -> userService.delete(1L), "user deletion threw an exception");

		logger.info("asserting that the single user cache got evicted");

		assertNull(client.get("user::" + 1L), "single user cache didnt got evicted");

		logger.info("asserting that the user page cache got evicted");

		assertTrue(findPageOfUsersFromCache().getContent().size() == 0, "user page cache didnt got evicted");

		logger.info("test success, user page and user deleted cache got evicted after user deletion");

	}

	private Page<UserDTOv2> findPageOfUsersFromService() {

		Pageable pageable = PageRequest.of(0, 20);

		return userService.findAllPaged(pageable);

	}

	private Page<UserDTOv2> findPageOfUsersFromCache() {

		String expectedCacheKey = "users::Page request [number: 0, size 20, sort: UNSORTED]";

		try {
			
			return ((Page<?>) client.get(expectedCacheKey)).map(x -> UserMapper.convertEntityToDtoV2((User) x));
			
		} catch (NullPointerException ex) {
			return Page.empty();
		}

	}

}
