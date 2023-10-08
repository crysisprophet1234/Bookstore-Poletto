package com.poletto.bookstore.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class CustomRedisClient<K, V> {

	private static final Logger logger = LoggerFactory.getLogger(CustomRedisClient.class);

	@Autowired
	RedisTemplate<K, V> redisTemplate;

	public CustomRedisClient() {
		super();
	}

	public V get(K key) {

		checkInput(key);

		try {

			return redisTemplate.opsForValue().get(key);

		} catch (Exception ex) {
			logger.warn("cache failure trying to get cache: {}", ex.getLocalizedMessage());
			return null;
		}

	}

	public boolean set(K key, V value) {

		checkInput(key, value);

		try {

			return redisTemplate.opsForValue().setIfAbsent(key, value);

		} catch (Exception ex) {
			logger.warn("cache failure trying to set cache: {}", ex.getLocalizedMessage());
			return false;
		}

	}

	public boolean put(K key, V value) {

		checkInput(key, value);

		try {

			return redisTemplate.opsForValue().setIfPresent(key, value);

		} catch (Exception ex) {
			logger.warn("cache failure trying to put cache: {}", ex.getLocalizedMessage());
			return false;
		}

	}

	public boolean del(K key) {

		checkInput(key);
		
		try {

			return redisTemplate.delete(key);
			
		} catch (Exception ex) {
			logger.warn("cache failure trying to delete cache: {}", ex.getLocalizedMessage());
			return false;
		}

	}

	public void clear() {
		redisTemplate.execute((RedisCallback<Object>) connection -> {
			connection.serverCommands().flushDb();
			return null;
		});
	}

	private <T> void checkInput(T input) {
		if (
				input == null ||
				input.toString().isBlank()
			) {
			throw new IllegalArgumentException("Key or value should not be empty or null");
		}
	}

	private <T, U> void checkInput(T firstInput, U secondInput) {
		if (
				firstInput == null ||
				firstInput.toString().isBlank() ||
				secondInput == null ||
				secondInput.toString().isBlank()
			) {
			throw new IllegalArgumentException("Key or value should not be empty or null");
		}
	}

}
