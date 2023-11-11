package com.poletto.bookstore.v3.services.caching;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.poletto.bookstore.converter.custom.UserMapper;
import com.poletto.bookstore.dto.v2.UserAuthDTOv2;
import com.poletto.bookstore.dto.v2.UserDTOv2;
import com.poletto.bookstore.entities.User;
import com.poletto.bookstore.repositories.v3.AuthRepository;
import com.poletto.bookstore.repositories.v3.RoleRepository;
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
public class AuthServiceCacheTest {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthServiceCacheTest.class);

	@Autowired
	private CustomRedisClient<String, User> client;

	private static RedisServer redisServer;

	@SpyBean
	private AuthRepository authRepository;
	
	@SpyBean
	private RoleRepository roleRepository;
	
	@SpyBean
	private UserRepository userRepository;
	
	@SpyBean
	private AuthenticationManager authenticationManager;

	@Autowired
	@InjectMocks
	private AuthService authService;
	
	@Autowired
	@InjectMocks
	private UserService userService;

	UserDTOv2 dto;
	UserAuthDTOv2 insertDto, loginDto;

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

		dto =  UserMocks.userMockDto();
		
		insertDto = UserMocks.registerUserMockDto();
		
		loginDto = UserMocks.loginUserMockDto(insertDto);

	}

	@AfterEach
	public void tearDown() {

		dto = insertDto = loginDto = null;

		client.clear();

	}

	@AfterAll
	public void cleanUp() {
		redisServer.stop();
	}
	
	@Test
	void isUserAuthBeingCachedAfterAuthentication() {
		
		logger.info("\n\n<=========  STARTING TEST isUserAuthBeingCachedAfterAuthentication()  =========>\n");
		
		logger.info("inserting a new user");
		
		assertDoesNotThrow(() -> authService.register(insertDto), "error thrown when trying to insert new user");
		
		logger.info("authenticating with new user [{}]", loginDto);

		assertDoesNotThrow(() -> loginDto = authService.authenticate(loginDto), "error thrown when trying to login with new user credentials");
		
		logger.info("querying userAuth from cache");
		
		User loginEntity = UserMapper.convertDtoToEntityV2(loginDto);
		
		User cachedLoginEntity =  client.get("userAuth::" + loginDto.getEmail());
		
		assertNotNull(cachedLoginEntity, "cachedLoginEntity was null");
		
		logger.info("asserting loginEntity and cachedLoginEntity are equals");
		
		assertEquals(loginEntity, cachedLoginEntity, "loginEntity and cachedLoginEntity were not equals");
		
		logger.info("updating loginEntity to check if userAuth cache gets evicted");
		
		loginDto.setPassword("newpassword");
		
		userService.update(loginEntity.getId(), loginDto);
		
		logger.info("asserting that the cache userAuth::{} got evicted", loginEntity.getEmail());
		
		assertNull(client.get("userAuth::" + loginEntity.getEmail()));
		
		logger.info("authenticating with updated user [{}]", loginEntity);
		
		assertDoesNotThrow(() -> loginDto = authService.authenticate(loginDto), "error thrown when trying to login with updated user credentials");
		
		loginEntity = UserMapper.convertDtoToEntityV2(loginDto);
		
		logger.info("asserting updated loginEntity and previous cachedLoginEntity are not equals");
		
		assertNotEquals(loginEntity.toString(), cachedLoginEntity.toString(), "updated loginEntity was equal to previous cachedLoginEntity");
		
		logger.info("querying userAuth from cache again");
		
		cachedLoginEntity = client.get("userAuth::" + loginEntity.getEmail());
		
		logger.info("asserting updated loginEntity and updated cachedLoginEntity are equals");
		
		assertEquals(loginEntity, cachedLoginEntity, "updated loginEntity was not equal to updated cachedLoginEntity");
		
		logger.info("asserting that the authenticationManager got invoked 2 times");
		
		verify(authenticationManager, times(2)).authenticate(any(UsernamePasswordAuthenticationToken.class));
		
		logger.info("test success, userAuth cache got properly set after a successful authentication and then evicted after user update");
		
	}
	
	@Test
	void isUsersPageGettingEvictedAfterUserRegistration() {
		
		logger.info("\n\n<=========  STARTING TEST isUsersPageGettingEvictedAfterUserRegistration()  =========>\n");
		
		logger.info("querying user page from repository");
		
		Page<User> userPage = findPageOfUsersFromService();
		
		assertNotNull(userPage, "userPage was null");
		
		logger.info("querying user page from cache");
		
		Page<User> cachedUserPage = findPageOfUsersFromCache();
		
		assertNotNull(cachedUserPage, "userPage was null");
		
		logger.info("asserting userPage from repository and cache are equals");
		
		assertEquals(userPage.getContent(), cachedUserPage.getContent(), "userPage from repository and cache were not equals");
		
		logger.info("inserting a new user to check if userPage cache gets evicted");
		
		assertDoesNotThrow(() -> authService.register(insertDto), "error thrown when trying to insert new user");
		
		logger.info("querying user page from repository again");
		
		userPage = findPageOfUsersFromService();
		
		assertNotNull(userPage, "userPage was null");
		
		logger.info("asserting updated userPage is not equals to previous cachedPage");
		
		assertNotEquals(userPage.getContent(), cachedUserPage.getContent(), "updated userPage is equals to previous userPageCached");
		
		logger.info("querying user page from cache again");
		
		cachedUserPage = findPageOfUsersFromCache();
		
		assertNotNull(cachedUserPage, "userPage was null");
		
		logger.info("asserting updated userPage is equals to updated cachedPage");
		
		assertEquals(userPage.getContent(), cachedUserPage.getContent(), "updated userPage is not equals to updated userPageCached");
		
		logger.info("asserting that the repository got invoked 2 times");
		
		verify(userRepository, times(2)).findAll(any(Pageable.class));
		
		logger.info("test success, cache got properly evicted and repository was invoked 2 times");
		
		
	}
	
	private Page<User> findPageOfUsersFromService() {

		Pageable pageable = PageRequest.of(0, 20);

		return userService.findAllPaged(pageable).map(x -> UserMapper.convertDtoToEntityV2(x));

	}

	private Page<User> findPageOfUsersFromCache() {

		String expectedCacheKey = "users::Page request [number: 0, size 20, sort: UNSORTED]";

		return ((Page<?>) client.get(expectedCacheKey)).map(x -> (User) x);

	}
	

}
