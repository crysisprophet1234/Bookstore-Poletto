package com.poletto.bookstore.services.v3.impl;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.controllers.v3.BookController;
import com.poletto.bookstore.converter.v3.BookMapperV3;
import com.poletto.bookstore.dto.v3.BookDto;
import com.poletto.bookstore.dto.v3.BookStatusUpdateDto;
import com.poletto.bookstore.dto.v3.BookUpdateDto;
import com.poletto.bookstore.dto.v3.CategoryDto;
import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.enums.BookStatus;
import com.poletto.bookstore.exceptions.InvalidStatusException;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.repositories.v3.AuthorRepository;
import com.poletto.bookstore.repositories.v3.BookRepository;
import com.poletto.bookstore.repositories.v3.CategoryRepository;
import com.poletto.bookstore.services.v3.BookService;

@Service("BookServiceV3")
public class BookServiceImpl implements BookService {
	
	private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Override
	@Transactional(readOnly = true)
	public Page<BookDto> findAll(Pageable pageable, Long categoryId, String name, String status) {
		
		Page<Book> bookEntitiesPage = bookRepository.findPaged(
				categoryId,
				name,
				status,
				pageable);

		logger.info("Resource BOOK page found: PAGE NUMBER [" + bookEntitiesPage.getNumber() + "] "
				  + "- CONTENT: " + bookEntitiesPage.getContent());

		Page<BookDto> bookDtoPage = bookEntitiesPage.map(bookEntity -> BookMapperV3.INSTANCE.bookToBookDto(bookEntity));

		bookDtoPage.stream().forEach(bookDto -> bookDtoWithLinks(bookDto));

		 return bookDtoPage;

	}

	@Override
	@Transactional(readOnly = true)
	public BookDto findById(Long bookId) {

		Book bookEntity = bookRepository.findById(bookId).orElseThrow(
				() -> new ResourceNotFoundException("Resource BOOK not found. ID " + bookId));
		
		logger.info("Resource BOOK found: " + bookEntity.toString());
		
		return bookDtoWithLinks(BookMapperV3.INSTANCE.bookToBookDto(bookEntity));
		
	}

	@Override
	@Transactional
	public BookDto insert(BookDto bookDto) {
		
		Book bookEntity = BookMapperV3.INSTANCE.bookDtoToBook(bookDto);
		
		Long authorId = bookDto.getAuthor().getId();
		
		bookEntity.setAuthor(authorRepository.findById(authorId).orElseThrow(
				() -> new ResourceNotFoundException("Resource AUTHOR not found. ID " + authorId)));
		
		bookEntity.getCategories().clear();
		
		for (CategoryDto categoryDto : bookDto.getCategories()) {
			
			Long categoryId = categoryDto.getId();
			
			bookEntity.getCategories().add(categoryRepository.findById(categoryId).orElseThrow(
					() -> new ResourceNotFoundException("Resource CATEGORY not found. ID " + categoryId)));
			
		}
		
		bookEntity.setStatus(BookStatus.ACTIVE);

		bookEntity = bookRepository.save(bookEntity);
		
		logger.info("Resource BOOK saved: " + bookEntity.toString());

		return bookDtoWithLinks(BookMapperV3.INSTANCE.bookToBookDto(bookEntity));

	}

	@Override
	@Transactional
	public BookDto update(Long bookId, BookUpdateDto bookDto) {

		Book bookEntity = bookRepository.findById(bookId).orElseThrow(
				() -> new ResourceNotFoundException("Resource BOOK not found. ID " + bookId));

		bookEntity = BookMapperV3.INSTANCE.updateBookWithBookUpdateDto(bookEntity, bookDto);
		
		Long authorId = bookDto.getAuthor().getId();
		
		//TODO: find a way to deal with null author id
		bookEntity.setAuthor(authorRepository.findById(authorId).orElseThrow(
				() -> new ResourceNotFoundException("Resource AUTHOR not found. ID " + authorId)));
		
		bookEntity.getCategories().clear();
		
		for (CategoryDto categoryDto : bookDto.getCategories()) {
			
			Long categoryId = categoryDto.getId();
			
			bookEntity.getCategories().add(categoryRepository.findById(categoryId).orElseThrow(
					() -> new ResourceNotFoundException("Resource CATEGORY not found. ID " + categoryId)));
			
		}

		bookEntity = bookRepository.save(bookEntity);
		
		logger.info("Resource BOOK updated: " + bookEntity.toString());

		return bookDtoWithLinks(BookMapperV3.INSTANCE.bookToBookDto(bookEntity));

	}
	
	@Override
	@Transactional
	public BookDto updateBookStatus(Long bookId, BookStatusUpdateDto bookStatusUpdateDto) {
		
		Book entity = bookRepository.findById(bookId).orElseThrow(
				() -> new ResourceNotFoundException("Resource BOOK not found. ID " + bookId));
		
		BookStatus newBookStatus = bookStatusUpdateDto.getBookStatus();
		
		if (entity.getStatus().equals(newBookStatus)) {
			throw new InvalidStatusException(entity);
		}
		
		entity.setStatus(newBookStatus);
		
		logger.info("Resource BOOK with ID: " + bookId + " updated bookStatus to: " + newBookStatus.toString());
		
		return bookDtoWithLinks(BookMapperV3.INSTANCE.bookToBookDto(entity));
		
	}
	
	private BookDto bookDtoWithLinks(BookDto bookDto) {
		return bookDto
				.add(linkTo(methodOn(BookController.class).findById(bookDto.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(BookController.class).findAllPaged(0, 12, "ASC", "ID", null, null, null)).withRel("all").withType("GET"))
				.add(linkTo(methodOn(BookController.class).update(bookDto.getId(), null)).withRel("update").withType("PUT"));
	}

}
