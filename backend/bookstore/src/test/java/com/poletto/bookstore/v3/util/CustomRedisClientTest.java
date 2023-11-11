package com.poletto.bookstore.v3.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.poletto.bookstore.util.CustomRedisClient;

import redis.embedded.RedisServer;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class CustomRedisClientTest {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomRedisClientTest.class);
	
	private static RedisServer redisServer;
	
	@Autowired
	CustomRedisClient<String, String> client;
	
	String keyMockString;
	String valueMockString;
	
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
	void setUp() {
		keyMockString = "key test string";
		valueMockString = "value test string";
	}

	@AfterEach
	void tearDown() {
		keyMockString = valueMockString = null;
		client.clear();
	}
	
	@AfterAll
	public void cleanUp() {
		redisServer.stop();
	}	

	@Test
	void testGetAndSet() {
		
		logger.info("\n\n<=========  STARTING TEST testGetAndSet()  =========>\n");
		
		logger.info("setting value on the cache");
		
		assertTrue(client.set(keyMockString, valueMockString), "setting cache value didnt worked");
		
		logger.info("retrieving the value on cache by its key");
		
		String valueFromCacheString = client.get(keyMockString);
		
		logger.info("asserting value from cache is not null");
		
		assertNotNull(valueFromCacheString, "value from cache is null");
		
		logger.info("asserting that mock = [{}] is equals to cache with key [{}]", valueMockString, keyMockString);
		
		assertEquals(valueMockString, valueFromCacheString, "cache didnt got setted properly");
		
		logger.info("test sucess, cache got setted and retrieved properly");
		
	}

	@Test
	void testPut() {
		
		logger.info("\n\n<=========  STARTING TEST testPut()  =========>\n");
		
		logger.info("setting value on the cache");
		
		assertTrue(client.set(keyMockString, valueMockString), "setting cache value didnt worked");
		
		logger.info("retrieving the value on cache by its key");
		
		String valueFromCacheString = client.get(keyMockString);
		
		logger.info("asserting value from cache is not null");
		
		assertNotNull(valueFromCacheString, "value from cache is null");
		
		logger.info("asserting that mock = [{}] is equals to cache with key [{}]", valueMockString, keyMockString);
		
		assertEquals(valueMockString, valueFromCacheString, "cache didnt got setted properly");
		
		logger.info("updating value on the cache");
		
		valueMockString = "updated value test string";
		
		assertTrue(client.put(keyMockString, valueMockString));
		
		logger.info("retrieving the value on cache by its key");
		
		valueFromCacheString = client.get(keyMockString);
		
		logger.info("asserting value from cache is not null");
		
		assertNotNull(valueFromCacheString, "value from cache is null");
		
		logger.info("asserting the cache got updated");
		
		assertEquals(valueMockString, valueFromCacheString, "cache didnt got updated properly");
		
		logger.info("test sucess, cache got updated properly");
		
	}

	@Test
	void testDel() {
		
		logger.info("\n\n<=========  STARTING TEST testDel()  =========>\n");
		
		logger.info("setting value on the cache");
		
		assertTrue(client.set(keyMockString, valueMockString), "setting cache value didnt worked");
		
		logger.info("retrieving the value on cache by its key");
		
		String valueFromCacheString = client.get(keyMockString);
		
		logger.info("asserting value from cache is not null");
		
		assertNotNull(valueFromCacheString, "value from cache is null");
		
		logger.info("asserting that mock = [{}] is equals to cache with key [{}]", valueMockString, keyMockString);
		
		assertEquals(valueMockString, valueFromCacheString, "cache didnt got setted properly");
		
		logger.info("deleting cache with key [{}]", keyMockString);
		
		assertTrue(client.del(keyMockString), "cache didnt got deleted");
		
		logger.info("test sucess, cache got deleted properly");
		
	}
	
	@Test
	void testExceptions() {
		
		logger.info("\n\n<=========  STARTING TEST testExceptions()  =========>\n");
		
		logger.info("asserting that clien.get com key vazia lanca excecao");
		
		assertThrows(IllegalArgumentException.class, () -> client.get(""), "client get com key vazia nao lancou execao");
		
		logger.info("asserting that client.get com key nula lanca excecao");
		
		assertThrows(IllegalArgumentException.class, () -> client.get(null), "client get com key nula nao lancou execao");
		
		logger.info("asserting that client.set com key e value vazios lanca excecao");
		
		assertThrows(IllegalArgumentException.class, () -> client.set("", ""), "client set com key e value vazios nao lancou execao");
		
		logger.info("asserting that client.set com key e value nulos lanca excecao");
		
		assertThrows(IllegalArgumentException.class, () -> client.set(null, null), "client set com key e value nulos nao lancou execao");
		
		logger.info("asserting that client.set com key preenchido e value vazio lanca excecao");
		
		assertThrows(IllegalArgumentException.class, () -> client.set("key", ""), "client set com key preenchido e value vazio nao lancou execao");
		
		logger.info("asserting that client.set com key preenchido e value nulo lanca excecao");
		
		assertThrows(IllegalArgumentException.class, () -> client.set("key", null), "client set com key preenchido e value nulo nao lancou execao");
		
		logger.info("asserting that client.set com key vazio e value preenchido lanca excecao");
		
		assertThrows(IllegalArgumentException.class, () -> client.set("", "value"), "client set com key vazio e value preenchido nao lancou execao");
		
		logger.info("asserting that client.set com key nulo e value preenchido lanca excecao");
		
		assertThrows(IllegalArgumentException.class, () -> client.set(null, "value"), "client set com key nulo e value preenchido nao lancou execao");
		
		logger.info("stopping redis server to check if Redis Connection failure gets properly handled");
		
		redisServer.stop();
		
		assertDoesNotThrow(() -> client.get("key"), "exception didnt get caught and stopped the client execution");
		
		redisServer.start();
		
		logger.info("test sucess, all parameters were correctly validated");
		
	}
	
}
