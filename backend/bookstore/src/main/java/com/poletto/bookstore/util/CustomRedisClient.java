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
		
		var value = redisTemplate.opsForValue().get(key);
		
		logger.info("Resource found on cache with key [{}] and value [{}]", key, value);
		
		return value;
		
	}

	public boolean set(K key, V value) {
		
		checkInput(key, value);
		
		var isUpdated = redisTemplate.opsForValue().setIfAbsent(key, value);
		
		logger.info("Resource stored on cache with key [{}] and value [{}]", key, value);
		
		return isUpdated;
		
	}

	public boolean put(K key, V value) {
		
		checkInput(key, value);
		
		var isSetted = redisTemplate.opsForValue().setIfPresent(key, value);
		
		logger.info("Resource updated on cache with key [{}] and value [{}]", key, value);
		
		return isSetted;
		
	}

	public boolean del(K key) {
		
		checkInput(key);
		
		var isDeleted =  redisTemplate.delete(key);
		
		logger.info("Resource deleted on cache with key [{}]", key);
		
		return isDeleted;
		
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
