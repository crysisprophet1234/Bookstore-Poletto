package com.poletto.bookstore.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.dto.BookDTO;
import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.repositories.BookRepository;
import com.poletto.bookstore.services.exceptions.ResourceNotFoundException;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	@Transactional(readOnly = true)
	public List<BookDTO> findAll() {
		List<Book> list = bookRepository.findAll();
		return list.stream().map(x -> new BookDTO(x)).toList(); //verificar sorted()
	}

	@Transactional(readOnly = true)
	public BookDTO findById(Long id) {
		Optional<Book> obj = bookRepository.findById(id);
		Book book = obj.orElseThrow(() -> new ResourceNotFoundException(id));
		return new BookDTO(book);
	}

}
