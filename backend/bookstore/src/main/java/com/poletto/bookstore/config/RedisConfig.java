package com.poletto.bookstore.config;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.hateoas.mediatype.MessageResolver;
import org.springframework.hateoas.mediatype.hal.CurieProvider;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule.HalHandlerInstantiator;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.hateoas.server.core.AnnotationLinkRelationProvider;
import org.springframework.hateoas.server.core.DelegatingLinkRelationProvider;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.poletto.bookstore.converter.CustomObjectMapper;
import com.poletto.bookstore.exceptions.handler.CustomCacheExceptionHandler;

@Configuration
public class RedisConfig implements CachingConfigurer  {

	@SuppressWarnings("unused")
	private GenericJackson2JsonRedisSerializer mapper() {

		CustomObjectMapper objectMapper = new CustomObjectMapper();

		objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), DefaultTyping.EVERYTHING, JsonTypeInfo.As.PROPERTY);
		
		LinkRelationProvider provider = new DelegatingLinkRelationProvider(new AnnotationLinkRelationProvider());

		objectMapper.registerModule(new Jackson2HalModule());
		objectMapper.setHandlerInstantiator(
				new HalHandlerInstantiator(provider, CurieProvider.NONE, MessageResolver.DEFAULTS_ONLY));
		
		return new GenericJackson2JsonRedisSerializer(objectMapper);
		
	}

	@Bean
	RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		//template.setValueSerializer(mapper());
		template.setValueSerializer(new JdkSerializationRedisSerializer());
		return template;
	}

	@Bean
	CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		RedisCacheManager redisCacheManager = new RedisCacheManager(
				RedisCacheWriter.lockingRedisCacheWriter(connectionFactory),
				cacheConfiguration()
		);
		return redisCacheManager;
	}

	@Bean
	RedisCacheConfiguration cacheConfiguration() {
		return RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofMinutes(60))
				.disableCachingNullValues()
				.serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
				//.serializeValuesWith(SerializationPair.fromSerializer(mapper()));
				.serializeValuesWith(SerializationPair.fromSerializer(new JdkSerializationRedisSerializer()));
	}
	
	@Bean
	public CacheErrorHandler errorHandler() {

		return new CustomCacheExceptionHandler();
		
	}

}