package com.poletto.bookstore.config;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.poletto.bookstore.services.v2.EmailService;
import com.poletto.bookstore.util.StackTraceFormatter;

@SuppressWarnings("deprecation")
@Configuration
public class CachingConfig extends CachingConfigurerSupport {

	private static final Logger logger = LoggerFactory.getLogger(CachingConfig.class);

	@Autowired
	private Environment env;
	
	@Autowired
	private EmailService emailService;

	@Override
	public CacheErrorHandler errorHandler() {

		return new CacheErrorHandler() {

			@Override
			public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
				logger.warn("cache failure trying to insert cache: {}", exception.getMessage());
				exceptionHandling(exception);
			}

			@Override
			public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
				logger.warn("cache failure trying to get cache: {}", exception.getMessage());
				exceptionHandling(exception);
			}

			@Override
			public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
				logger.warn("cache failure trying to evict cache: {}", exception.getMessage());
				exceptionHandling(exception);

			}

			@Override
			public void handleCacheClearError(RuntimeException exception, Cache cache) {
				logger.warn("cache failure trying to clear cache: {}", exception.getMessage());
				exceptionHandling(exception);
			}

		};

	}

	private void exceptionHandling(Exception ex) {
		
		logger.error(StackTraceFormatter.stackTraceFormatter(ex));

		if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
			
			emailService.sendEmail(
					"polettobookstore@gmail.com",
					"Redis server failure on production enviroment",
					"Main instance of poletto-bookstore API unnable to connect to Redis Server in production enviroment at " + LocalDateTime.now()
				  + "\nFollows below the stack trace for the error:"
				  + "\n\n" + StackTraceFormatter.stackTraceFormatter(ex)
			);

		}

	}

}
