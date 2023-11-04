package com.poletto.bookstore.v3.services.caching;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import com.poletto.bookstore.dto.v2.BookDTOv2;
import com.poletto.bookstore.dto.v2.ReservationDTOv2;
import com.poletto.bookstore.repositories.v3.BookRepository;
import com.poletto.bookstore.repositories.v3.ReservationRepository;
import com.poletto.bookstore.services.v3.BookService;
import com.poletto.bookstore.services.v3.ReservationService;
import com.poletto.bookstore.util.CustomRedisClient;
import com.poletto.bookstore.v3.mocks.BookMocks;
import com.poletto.bookstore.v3.mocks.ReservationMocks;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CacheExceptionTest {

	private static final Logger logger = LoggerFactory.getLogger(CacheExceptionTest.class);

	@Autowired
	private CustomRedisClient<String, Object> client;

	@SpyBean
	private BookRepository bookRepository;

	@Autowired
	@InjectMocks
	private BookService bookService;
	
	@SpyBean
	private ReservationRepository reservationRepository;

	@Autowired
	@InjectMocks
	private ReservationService reservationService;

	BookDTOv2 bookDTO, bookInsertDto;
	
	ReservationDTOv2 reservationDto, insertReservationDto;

	@BeforeEach
	public void setUp() {

		bookInsertDto = BookMocks.insertBookMockDto();

		bookDTO = BookMocks.bookMockDto(1L);
		
		reservationDto = ReservationMocks.reservationMockDto(1L);
		
		insertReservationDto = ReservationMocks.insertReservationMockDto();

	}

	@AfterEach
	public void tearDown() {

		bookDTO = bookInsertDto = null;
		
		reservationDto = insertReservationDto = null;	

	}

	@Test
	void isBookServiceBeingCalledIfCacheErrorIsThrown() {

		logger.info("\n\n<=========  STARTING TEST isBookServiceBeingCalledIfCacheErrorIsThrown()  =========>\n");

		logger.info("calling the service insert(insertDto) to check if it throws an exception");

		assertDoesNotThrow(() -> bookDTO = bookService.insert(bookInsertDto), "service insert(insertDto) threw an exception");

		logger.info("calling the service findById(1) to check if it throws an exception");

		assertDoesNotThrow(() -> bookDTO = bookService.findById(1L), "service findById(1) threw an exception");

		assertNotNull(bookDTO, "book findById(1) returned null");

		logger.info("retrieved dto on the repository = {}", bookDTO);

		logger.info("calling the service delete(1L) to check if it throws an exception");

		assertDoesNotThrow(() -> bookService.delete(bookDTO.getId()), "service delete(1L) threw an exception");

		logger.info("asserting that the repository got called 3 times");

		verify(bookRepository, times(1)).findById(1L);

		verify(bookRepository, times(1)).save(any());

		verify(bookRepository, times(1)).deleteById(1L);

		logger.info("test success, book operation on the repository were executed even without redis server up");

	}

	@Test
	void isClientRespondingWhenRedisServerIsDown() {

		logger.info("\n\n<=========  STARTING TEST isClientRespondingWhenRedisServerIsDown()  =========>\n");

		logger.info("trying to insert cache with the client");

		assertDoesNotThrow(() -> client.set("book#1", bookDTO), "client threw an error when trying to get");

		logger.info("trying to retrieve cache with the client");

		assertDoesNotThrow(() -> client.get("book#1"), "client threw an error when trying to get");

		logger.info("trying to update cache with the client");

		assertDoesNotThrow(() -> client.put("book#1", bookDTO), "client threw an error when trying to update");

		logger.info("trying to delete cache with the client");

		assertDoesNotThrow(() -> client.del("book#1"), "client threw an error when trying to delete");
		
		logger.info("test success, client operations didnt get executed and threw the RedisConnectionFailureException");

	}

	@Test
	void isReservationServiceGettingExecutedIfCacheErrorIsThrown() {
		
		logger.info("\n\n<=========  STARTING TEST isReservationServiceGettingExecutedIfCacheErrorIsThrown()  =========>\n");
		
		logger.info("calling the service insert(bookInsertDto) to check if it throws an exception");

		assertDoesNotThrow(() -> bookDTO = bookService.insert(bookInsertDto), "service insert(bookInsertDto) threw an exception");
		
		insertReservationDto = ReservationMocks.insertReservationMockDto(bookDTO.getId());
		
		logger.info("calling the service reserveBooks(insertReservationDto) to check if it throws an exception");

		assertDoesNotThrow(() -> reservationDto = reservationService.reserveBooks(insertReservationDto), "service reserveBooks(insertReservationDto) threw an exception");
		
		logger.info("reservation service worked properly = {}", reservationDto);
		
		logger.info("calling the service findById(1) to check if it throws an exception");

		assertDoesNotThrow(() -> reservationDto = reservationService.findById(1L), "service findById(1) threw an exception");
		
		assertNotNull(reservationDto, "reservation findById(1) returned null");

		reservationService.returnReservation(1L);

	}
	
}
