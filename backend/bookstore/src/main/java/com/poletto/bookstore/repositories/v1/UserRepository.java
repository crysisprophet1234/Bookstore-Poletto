package com.poletto.bookstore.repositories.v1;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poletto.bookstore.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByEmail(String email);

}
