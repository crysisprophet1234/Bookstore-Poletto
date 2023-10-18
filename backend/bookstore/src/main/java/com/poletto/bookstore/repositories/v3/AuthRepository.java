package com.poletto.bookstore.repositories.v3;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poletto.bookstore.entities.User;

@Repository("AuthRepositoryV3")
public interface AuthRepository extends JpaRepository<User, Long> {
	
	@Cacheable(value = "userAuth", key = "#email")
	Optional<User> findByEmail(String email);
	
	@CacheEvict(value = "users", allEntries = true)
	<S extends User> S save(S entity);

}
