package com.poletto.bookstore.repositories.v3;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poletto.bookstore.entities.Category;
	
@Repository("CategoryRepositoryV3")
public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	List<Category> findAll();
	
	Page<Category> findAll(Pageable pageable);
	
	@Cacheable(value = "category", key = "#id")
    Optional<Category> findById(Long id);
	
}
