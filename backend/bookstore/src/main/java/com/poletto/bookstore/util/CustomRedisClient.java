package com.poletto.bookstore.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class CustomRedisClient<K, V> {

	@Autowired
	RedisTemplate<K, V> redisTemplate;

	public CustomRedisClient() {
		super();
	}

	public V get(K key) {
		checkInput(key);
		return redisTemplate.opsForValue().get(key);
	}

	public boolean set(K key, V value) {
		checkInput(key, value);
		return redisTemplate.opsForValue().setIfAbsent(key, value);
	}

	public boolean put(K key, V value) {
		checkInput(key, value);
		return redisTemplate.opsForValue().setIfPresent(key, value);
	}

	public boolean del(K key) {
		checkInput(key);
		return redisTemplate.delete(key);
	}

	public void flushDb() {
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
