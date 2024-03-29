package com.poletto.bookstore.repositories.v2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poletto.bookstore.entities.Book;

@Repository("BookRepositoryV2")
public interface BookRepository extends JpaRepository<Book, Long> {
	
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
	
}
