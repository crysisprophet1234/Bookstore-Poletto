package com.poletto.bookstore.repositories.v3;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poletto.bookstore.entities.User;


@Repository("UserRepositoryV3")
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Cacheable("users")
	Page<User> findAll(Pageable pageable);
	
	@Cacheable(value = "user", key = "#id")
	Optional<User> findById(Long id);
	
	@Caching(
			evict = { @CacheEvict(value = {"users", "reservations", "reservation"}, allEntries = true) },
			put = { @CachePut(value = "user", key = "#entity.id") }
	)
	<S extends User> S save(S entity);
	
	@Caching(evict = { 
			@CacheEvict(value = "users", allEntries = true),
			@CacheEvict(value = "user", key = "#id")
	})
	void deleteById(Long id);

}