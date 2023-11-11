package com.poletto.bookstore.repositories.v3;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poletto.bookstore.entities.Book;

@Repository("BookRepositoryV3")
public interface BookRepository extends JpaRepository<Book, Long> {
	
	@Cacheable(value = "books")
	@Query("SELECT DISTINCT obj "
		 + "FROM Book obj "
		 + "INNER JOIN obj.categories cats "
		 + "WHERE (:categoryId IS NULL OR cats.id IN :categoryId) "
		 + "AND (:name IS NULL OR LOWER(obj.name) LIKE LOWER(CONCAT('%', :name, '%')) "
		 + "OR LOWER(obj.author.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
		 + "AND (:status = 'ALL' OR obj.status = :status)")
	Page<Book> findPaged(
	        @Param("categoryId") Long categoryId,
	        @Param("name") String name,
	        @Param("status") String status,
	        Pageable pageable
	);
	
	@Cacheable(value = "book", key = "#id")
	Optional<Book> findById(Long id);
	
	@Caching(
			evict = { @CacheEvict(value = "books", allEntries = true) },
			put = { @CachePut(value = "book", key = "#entity.id", condition = "#entity.id != null") }
	)
	<S extends Book> S save(S entity);
	
	@Caching(evict = {
			@CacheEvict(value = "books", allEntries = true),
			@CacheEvict(value = "book", key = "#id")
	})
	void deleteById(Long id);
	
	
	
}
