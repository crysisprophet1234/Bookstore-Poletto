package com.poletto.bookstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.poletto.bookstore.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByEmail(String email);

}
