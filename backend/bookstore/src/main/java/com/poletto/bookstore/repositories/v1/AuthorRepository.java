package com.poletto.bookstore.repositories.v1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poletto.bookstore.entities.Author;

@Repository("AuthorRepositoryV1")
public interface AuthorRepository extends JpaRepository<Author, Long> {
	
}
