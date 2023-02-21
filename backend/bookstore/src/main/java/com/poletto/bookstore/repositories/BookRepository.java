package com.poletto.bookstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poletto.bookstore.entities.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
