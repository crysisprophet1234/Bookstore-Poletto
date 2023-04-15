package com.poletto.bookstore.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.converter.custom.BookMapper;
import com.poletto.bookstore.dto.BookDTO;
import com.poletto.bookstore.dto.CategoryDTO;
import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.Category;
import com.poletto.bookstore.entities.enums.BookStatus;
import com.poletto.bookstore.repositories.AuthorRepository;
import com.poletto.bookstore.repositories.BookRepository;
import com.poletto.bookstore.repositories.CategoryRepository;
import com.poletto.bookstore.services.exceptions.DatabaseException;
import com.poletto.bookstore.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BookService {
	
	private static final Logger logger = LoggerFactory.getLogger(BookService.class);

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Deprecated
	@Transactional(readOnly = true)
	public List<BookDTO> findAll() {

		List<Book> list = bookRepository.findAll();

		return list.stream().map(x -> new BookDTO(x)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Page<BookDTO> findAllPaged(Pageable pageable, Long categoryId, String name, String booked) {

		List<Category> categories = (categoryId == 0) ? null
				: Arrays.asList(categoryRepository.getReferenceById(categoryId));

		switch (booked) {

		case "available":
			booked = "BOOKED";
			break;

		case "booked":
			booked = "AVAILABLE";
			break;

		case "":
			booked = "";
			break;

		}

		var bookPage = bookRepository.findPaged(categories, name, booked, pageable);

		bookRepository.findProductsWithCategories(bookPage.getContent());
		
		logger.info("Resource BOOK page found: " + "PAGE NUMBER [" + bookPage.getNumber() + "] - CONTENT: " + bookPage.getContent());

		return bookPage.map(x -> BookMapper.convertEntityToDto(x));

	}

	@Transactional(readOnly = true)
	public BookDTO findById(Long id) {
		
		Optional<Book> obj = bookRepository.findById(id);
		
		Book book = obj.orElseThrow(() -> new ResourceNotFoundException(id, "Book"));
		
		logger.info("Resource BOOK found: " + book.toString());
		
		return BookMapper.convertEntityToDto(book);
		
	}

	@Transactional
	public BookDTO insert(BookDTO dto) {
		
		dto.setStatus(BookStatus.AVAILABLE);
		
		dto.setAuthor(authorRepository.findById(dto.getAuthor().getId()).orElseThrow(() -> new ResourceNotFoundException(dto.getAuthor().getId(), "Book")));

		Book entity = BookMapper.convertDtoToEntity(dto);
		
		entity.getCategories().clear();
		
		for (CategoryDTO categoryDTO : dto.getCategories()) {
			try {
			Category category = categoryRepository.getReferenceById(categoryDTO.getId());
			entity.getCategories().add(category);
			} catch (EntityNotFoundException ex) {
				throw new ResourceNotFoundException(categoryDTO.getId(), "Book");
			}
		}

		entity = bookRepository.save(entity);
		
		logger.info("Resource BOOK saved: " + entity.toString());

		return BookMapper.convertEntityToDto(entity);

	}

	@Transactional
	public BookDTO update(Long id, BookDTO dto) {

		try {
			
			Book entity = bookRepository.getReferenceById(id);
			
			dto.setStatus(entity.getStatus());
			
			dto.setId(entity.getId());

			entity = BookMapper.convertDtoToEntity(dto);

			entity = bookRepository.save(entity);
			
			logger.info("Resource BOOK updated: " + entity.toString());

			return BookMapper.convertEntityToDto(entity);

		} catch (EntityNotFoundException e) {

			throw new ResourceNotFoundException(id, "Book");

		}

	}

	public void delete(Long id) {

		try {

			bookRepository.deleteById(id);
			
			logger.info("Resource BOOK deleted: ID " + id);

		} catch (EmptyResultDataAccessException e) {

			throw new ResourceNotFoundException(id, "Book");

		} catch (DataIntegrityViolationException e) {

			throw new DatabaseException("Integrity violation");

		}

	}

}
