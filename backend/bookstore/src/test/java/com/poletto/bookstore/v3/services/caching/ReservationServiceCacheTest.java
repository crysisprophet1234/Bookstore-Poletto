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

import java.time.LocalDate;
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

import com.poletto.bookstore.converter.custom.ReservationMapper;
import com.poletto.bookstore.dto.v2.ReservationDTOv2;
import com.poletto.bookstore.entities.Reservation;
import com.poletto.bookstore.entities.enums.ReservationStatus;
import com.poletto.bookstore.repositories.v3.ReservationRepository;
import com.poletto.bookstore.services.v3.BookService;
import com.poletto.bookstore.services.v3.ReservationService;
import com.poletto.bookstore.util.CustomRedisClient;
import com.poletto.bookstore.v3.mocks.BookMocks;
import com.poletto.bookstore.v3.mocks.ReservationMocks;

import redis.embedded.RedisServer;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(Lifecycle.PER_CLASS)
public class ReservationServiceCacheTest {

	private static final Logger logger = LoggerFactory.getLogger(ReservationServiceCacheTest.class);

	@Autowired
	private CustomRedisClient<String, Reservation> client;
	
	private static RedisServer redisServer;

	@SpyBean
	private ReservationRepository repository;

	@Autowired
	@InjectMocks
	private ReservationService service;
	
	@Autowired
	private BookService bookService;

	private ReservationDTOv2 insertDto, dto;
	private Reservation entity;
	
	@BeforeAll
	public void startRedis() {
		
		try {
			redisServer = RedisServer.builder()
					.port(6370)
					.setting("maxmemory 128M")
					.build();
			redisServer.start();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
	}

	@BeforeEach
	public void setUp() {
		
		bookService.insert(BookMocks.insertBookMockDto());
		
		dto = ReservationMocks.reservationMockDto(1L);
		
		insertDto = ReservationMocks.insertReservationMockDto();
		
		entity = ReservationMocks.reservationMockEntity();

		service.reserveBooks(insertDto);
		
		client.clear();
		
	}

	@AfterEach
	public void tearDown() {
		
		insertDto = dto = null;
		
		entity = null;
		
		client.clear();
		
	}
	
	@AfterAll
	public void cleanUp() {
		redisServer.stop();
	}

	@Test
	void isCacheBeingSavedAfterQueryReservationById() {
		
		logger.info("\n\n<=========  STARTING TEST isCacheBeingSavedAfterQueryReservationById()  =========>\n");
		
		logger.info("querying reservation with id 1 from the repository");

		dto = service.findById(1L);
		
		assertNotNull(dto, "reservation with id 1 not found on db");

		ReservationDTOv2 cachedDto = ReservationMapper.convertEntityToDtoV2(client.get("reservation::1"));
		
		assertNotNull(dto, "reservation with id 1 not found on cache");
		
		logger.info("asserting that dto from db and cache are equals");

		assertEquals(dto.toString(), cachedDto.toString(), "dto and cachedDto were not equals");
		
		logger.info("querying reservation with id 1 again to assert that the cache will get hit");
		
		assertEquals(dto, service.findById(1L));
		
		logger.info("asserting that cache got hit");

		verify(repository, times(1)).findById(1L);
		
		logger.info("test sucess, service invoked 2 times and repository only 1");

	}

	@Test
	void isCacheBeingUpdatedAfterReturnReservation() {
		
		logger.info("\n\n<=========  STARTING TEST isCacheBeingUpdatedAfterReturnReservation()  =========>\n");
		
		logger.info("querying reservation with id 1 from the repository");

		dto = service.findById(1L);
		
		assertNotNull(dto, "reservation with id 1 not found on repository");
		
		logger.info("querying reservation with id 1 from cache");
		
		ReservationDTOv2 cachedDto = ReservationMapper.convertEntityToDtoV2(client.get("reservation::1"));
		
		assertNotNull(cachedDto, "reservation with id 1 not found on cache");
		
		logger.info("asserting that dto from db and cache are equals");

		assertEquals(dto.toString(), cachedDto.toString(), "dto and cachedDto were not equals");
		
		logger.info("returning reservation with id 1");

		assertDoesNotThrow(() -> service.returnReservation(1L), "reservation return threw an exception");
		
		logger.info("asserting that reservation 1 cache got evicted");
		
		assertNull(client.get("reservation::1"), "cache reservation 1 didnt got evicted");
		
		logger.info("querying reservation with id 1 again");

		dto = service.findById(1L);
		
		logger.info("asserting that the cache got updated");

		assertNotEquals(dto.toString(), cachedDto.toString(), "new cache was equals to previous cache value");
		
		logger.info("querying reservation with id 1 from cache again");

		cachedDto = ReservationMapper.convertEntityToDtoV2(client.get("reservation::1"));
		
		logger.info("asserting that updated dto from db and cache are equals");

		assertEquals(dto.toString(), cachedDto.toString(), "updated dto and cachedDto were not equals");
		
		logger.info("calling repository again to check if cache gets hit");
		
		service.findById(1L);
		
		logger.info("asserting that repository got invoked only 2 times");

		verify(repository, times(2)).findById(1L);
		
		logger.info("test sucess, service invoked 3 times and repository only 2, cache got evicted after reservation return");

	}

	@Test
	void isPageCacheBeingEvictedAfterNewReservationsAndReturns() {
		
		logger.info("\n\n<=========  STARTING TEST isPageCacheBeingEvictedAfterNewReservationsAndReturns()  =========>\n");
		
		logger.info("querying page of books on the repository");

		Page<ReservationDTOv2> dtoPage = findPageOfReservationsFromService();
		
		assertNotNull(dtoPage, "dtoPage not found on repository");

		assertNotEquals(List.of().size(), dtoPage.getContent().size(), "dtoPage found but with 0 elements");
		
		logger.info("dtoPage retrieved from db: {}", dtoPage.getContent());
		
		logger.info("querying dtoPage again to assert that the cache will get hit");

		Page<ReservationDTOv2> cachedDtoPage = findPageOfReservationsFromCache();
		
		assertNotNull(cachedDtoPage, "dtoPage with not found on cache");
		
		assertNotEquals(List.of().size(), cachedDtoPage.getContent().size(), "cacheDtoPage found but with 0 elements");
		
		logger.info("dtoPage retrieved from cache: {}", cachedDtoPage.getContent());

		assertEquals(
				dtoPage.getContent().get(0).toString(),
				cachedDtoPage.getContent().get(0).toString(),
				"dtoPage from db and cache are not equals"
		);
		
		logger.info("querying dtoPage from repo again to assert that the cache will get hit");

		findPageOfReservationsFromService();
		
		logger.info("asserting that the repository will not get a call");

		verify(repository, times(1)).findPaged(any(), any(), any(), any(), any(), any());
		
		logger.info("returning reservation with id 1 to check if the cache gets updated");

		service.returnReservation(1L);
		
		logger.info("querying dtoPage again to assert that the repo will get called");

		dtoPage = findPageOfReservationsFromService();
		
		verify(repository, times(2)).findPaged(any(), any(), any(), any(), any(), any());
		
		logger.info("asserting that the new dtoPage is not equal to the old dtoPage from cache");
		
		logger.info("dtoPage after update = {}, old cachedDtoPage = {}", dtoPage.getContent(), cachedDtoPage.getContent());
		
		assertNotEquals(
				dtoPage.getContent().get(0).toString(),
				cachedDtoPage.getContent().get(0).toString(),
				"cache didnt get evicted after return method got called"
		);
		
		logger.info("inserting new reservation to check if the cache gets updated");

		service.reserveBooks(insertDto);
		
		logger.info("querying dtoPage again to assert that the repo will get called one more time");

		dtoPage = findPageOfReservationsFromService();

		verify(repository, times(3)).findPaged(any(), any(), any(), any(), any(), any());
		
		logger.info("test sucess, service called 6 times and repository only 3");

	}
	
	@Test
	void isCacheGettingProperlySettedUsingClient() {
		
		logger.info("\n\n<=========  STARTING TEST isCacheGettingProperlySettedUsingClient()  =========>\n");
		
		String reservationKey = "reservation#1";
		
		logger.info("setting on the cache value [{}] with key [{}]", entity, reservationKey);
		
		assertTrue(client.set(reservationKey, entity), "cache didnt got setted");
		
		logger.info("asserting that the cache value is not null");
		
		assertNotNull(client.get(reservationKey));
		
		logger.info("asserting that the cache value is equals to bookDto");
		
		assertEquals(entity, client.get(reservationKey), "cache value didnt get setted properly");
		
		logger.info("updating value on the cache");
		
		entity.setStatus(ReservationStatus.FINISHED);
		
		assertTrue(client.put(reservationKey, entity), "cache value didnt get updated");
		
		logger.info("asserting that the value got updated");
		
		assertEquals(entity.getStatus(), client.get(reservationKey).getStatus());
		
		logger.info("test success, cache got setted, retrieved and updated properly");
		
	}
	
	private Page<ReservationDTOv2> findPageOfReservationsFromService() {

		Pageable pageable = PageRequest.of(0, 12);
		
		LocalDate startingDate = LocalDate.of(2020, 1, 1);

		LocalDate devolutionDate = LocalDate.of(2099, 1, 1);

		return service.findAllPaged(pageable, startingDate, devolutionDate, null, null, "all");

	}

	private Page<ReservationDTOv2> findPageOfReservationsFromCache() {

		String expectedCacheKey = "reservations::SimpleKey [2020-01-01T03:00:00Z,2099-01-01T03:00:00Z,null,null,ALL,Page request [number: 0, size 12, sort: UNSORTED]]";
		
		try {

			return ((Page<?>) client.get(expectedCacheKey)).map(x -> ReservationMapper.convertEntityToDtoV2((Reservation) x));
		
		} catch (NullPointerException ex) {
			return Page.empty();
		}

	}

}
