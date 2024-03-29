package com.poletto.bookstore.services.v2;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.controllers.v2.BookController;
import com.poletto.bookstore.converter.custom.BookMapper;
import com.poletto.bookstore.dto.v2.BookDTOv2;
import com.poletto.bookstore.dto.v2.CategoryDTOv2;
import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.Category;
import com.poletto.bookstore.entities.enums.BookStatus;
import com.poletto.bookstore.exceptions.DatabaseException;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.repositories.v2.AuthorRepository;
import com.poletto.bookstore.repositories.v2.BookRepository;
import com.poletto.bookstore.repositories.v2.CategoryRepository;

import jakarta.persistence.EntityNotFoundException;

@Service("BookServiceV2")
public class BookService {

	private static final Logger logger = LoggerFactory.getLogger(BookService.class);

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Transactional(readOnly = true)
	public Page<BookDTOv2> findAllPaged(Pageable pageable, Long categoryId, String name, String status) {

		Page<Book> bookPage = bookRepository.findPaged(
				categoryId, 
				name.toLowerCase(), 
				status.toUpperCase(), 
				pageable);

		logger.info("Resource BOOK page found: PAGE NUMBER [" + bookPage.getNumber() + "] "
				  + "- CONTENT: " + bookPage.getContent());

		Page<BookDTOv2> dtosPage = bookPage.map(x -> BookMapper.convertEntityToDtoV2(x));

		dtosPage.stream().forEach(x -> x
				.add(linkTo(methodOn(BookController.class).findById(x.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(BookController.class).delete(x.getId())).withRel("delete").withType("DELETE"))
				.add(linkTo(methodOn(BookController.class).update(x.getId(), x)).withRel("update").withType("PUT")));

		return dtosPage;

	}

	@Transactional(readOnly = true)
	public BookDTOv2 findById(Long id) {

		Optional<Book> obj = bookRepository.findById(id);

		Book book = obj.orElseThrow(() -> new ResourceNotFoundException("Resource BOOK not found. ID " + id));

		logger.info("Resource BOOK found: " + book.toString());

		BookDTOv2 dto = BookMapper.convertEntityToDtoV2(book);

		dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
		dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
		dto.add(linkTo(methodOn(BookController.class).update(dto.getId(), dto)).withRel("update").withType("PUT"));

		return dto;

	}
	
	//testar

	@Transactional
	public BookDTOv2 insert(BookDTOv2 dto) {
		
		dto.setStatus(BookStatus.AVAILABLE);

		Book entity = BookMapper.convertDtoToEntityV2(dto);
		
		entity.setAuthor(authorRepository.findById(dto.getAuthor().getId()).orElseThrow(
				() -> new ResourceNotFoundException("Resource AUTHOR not found. ID " + dto.getAuthor().getId())));

		entity.getCategories().clear();

		for (CategoryDTOv2 categoryDTO : dto.getCategories()) {

			Category category = categoryRepository.findById(categoryDTO.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Resource CATEGORY not found. ID " + categoryDTO.getId()));
			
			entity.getCategories().add(category);

		}

		entity = bookRepository.save(entity);

		logger.info("Resource BOOK saved: " + entity.toString());

		BookDTOv2 newDto = BookMapper.convertEntityToDtoV2(entity);

		newDto.add(linkTo(methodOn(BookController.class).findById(newDto.getId())).withSelfRel().withType("GET"));
		newDto.add(linkTo(methodOn(BookController.class).delete(newDto.getId())).withRel("delete").withType("DELETE"));
		newDto.add(linkTo(methodOn(BookController.class).update(newDto.getId(), newDto)).withRel("update")
				.withType("PUT"));

		return newDto;

	}

	@Transactional
	public BookDTOv2 update(Long id, BookDTOv2 dto) {

		try {

			Book entity = bookRepository.getReferenceById(id);
			
			dto.setStatus(entity.getStatus());

			entity = BookMapper.convertDtoToEntityV2(dto);

			entity = bookRepository.save(entity);

			dto = BookMapper.convertEntityToDtoV2(entity);
			
			logger.info("Resource BOOK updated: " + dto.toString());

			dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
			dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
			dto.add(linkTo(methodOn(BookController.class).update(dto.getId(), dto)).withRel("update").withType("PUT"));

			return dto;

		} catch (EntityNotFoundException e) {

			throw new ResourceNotFoundException("Resource BOOK not found. ID " + id);

		}

	}

	public void delete(Long id) {

		try {

			if (bookRepository.existsById(id)) {
				bookRepository.deleteById(id);
			} else {
				throw new ResourceNotFoundException("Resource BOOK not found. ID " + id);
			}

			logger.info("Resource BOOK deleted: ID " + id);

		} catch (DataIntegrityViolationException e) {

			throw new DatabaseException(e.getMessage());

		}

	}

}
