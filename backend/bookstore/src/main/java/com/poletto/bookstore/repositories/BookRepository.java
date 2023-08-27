package com.poletto.bookstore.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.Category;

public interface BookRepository extends JpaRepository<Book, Long> {

	List<Book> findByStatus(String status);
	
	//TODO: VERSIONATE!!!
	@Deprecated
	@Query("SELECT DISTINCT obj FROM Book obj INNER JOIN obj.categories cats WHERE "
	        + "(COALESCE(:categories IS NULL OR cats IN :categories) AND "
	        + "(LOWER(obj.name) LIKE LOWER(CONCAT('%', :name, '%')) "
	        + "OR LOWER(obj.author.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
	        + "AND (:booked IS NULL OR obj.status NOT IN (:booked)))")
	Page<Book> oldFindPaged(
	        @Param("categories") List<Category> categories,
	        @Param("name") String name,
	        @Param("booked") String booked,
	        Pageable pageable
	);
	
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

	
	@Deprecated
	@Query("SELECT obj FROM Book obj JOIN FETCH obj.categories WHERE obj IN :products")
	List<Book> findProductsWithCategories(@Param("products")List<Book> products);
	
}
