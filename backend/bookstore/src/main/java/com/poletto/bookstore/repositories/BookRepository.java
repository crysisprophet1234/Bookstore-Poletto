package com.poletto.bookstore.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.Category;

public interface BookRepository extends JpaRepository<Book, Long> {

	List<Book> findByStatus(String status);
	
	@Query("SELECT DISTINCT obj FROM Book obj INNER JOIN obj.categories cats WHERE "
			+ "(COALESCE(:categories) IS NULL OR cats IN :categories) AND "
			+ "(LOWER(obj.name) LIKE LOWER(CONCAT('%',:name,'%'))) ")
	Page<Book> findPaged(List<Category> categories, String name, Pageable pageable);
	
	@Query(value = "SELECT obj FROM Book obj JOIN FETCH obj.categories WHERE obj IN :products")
	List<Book> findProductsWithCategories(List<Book> products);
	
}
