package com.poletto.bookstore.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poletto.bookstore.entities.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

	List<Book> findByStatus(String status);
	
}
