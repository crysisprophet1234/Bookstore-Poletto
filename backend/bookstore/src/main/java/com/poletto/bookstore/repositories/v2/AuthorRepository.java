package com.poletto.bookstore.repositories.v2;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poletto.bookstore.entities.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
	
}
