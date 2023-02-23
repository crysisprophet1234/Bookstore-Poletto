package com.poletto.bookstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poletto.bookstore.entities.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
	
}
