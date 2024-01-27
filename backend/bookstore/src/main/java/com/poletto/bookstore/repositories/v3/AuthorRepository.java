package com.poletto.bookstore.repositories.v3;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poletto.bookstore.entities.Author;

@Repository("AuthorRepositoryV3")
public interface AuthorRepository extends JpaRepository<Author, Long> {
	
	List<Author> findAll();
	
	Page<Author> findAll(Pageable pageable);
	
	@Cacheable(value = "author", key = "#id")
	Optional<Author> findById(Long id);
	
}
