package com.poletto.bookstore.services.v3;

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
import com.poletto.bookstore.converter.custom.BookMapper;
import com.poletto.bookstore.dto.v2.BookDTOv2;
import com.poletto.bookstore.dto.v2.CategoryDTOv2;
import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.Category;
import com.poletto.bookstore.entities.enums.BookStatus;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.repositories.v3.AuthorRepository;
import com.poletto.bookstore.repositories.v3.BookRepository;
import com.poletto.bookstore.repositories.v3.CategoryRepository;

@Service("BookServiceV3")
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

		Page<Book> entitiesPage = bookRepository.findPaged(
				categoryId,
				name.toLowerCase(),
				status.toUpperCase(),
				pageable);

		logger.info("Resource BOOK page found: PAGE NUMBER [" + entitiesPage.getNumber() + "] "
				  + "- CONTENT: " + entitiesPage.getContent());

		Page<BookDTOv2> dtosPage = entitiesPage.map(x -> BookMapper.convertEntityToDtoV2(x));

		 dtosPage.stream().forEach(dto -> dto
				.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"))
				.add(linkTo(methodOn(BookController.class).update(dto.getId(), dto)).withRel("update").withType("PUT")));

		 return dtosPage;

	}

	@Transactional(readOnly = true)
	public BookDTOv2 findById(Long id) {

		Book entity = bookRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Resource BOOK not found. ID " + id));
		
		logger.info("Resource BOOK found: " + entity.toString());
		
		return BookMapper.convertEntityToDtoV2(entity)
				.add(linkTo(methodOn(BookController.class).findById(entity.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(BookController.class).delete(entity.getId())).withRel("delete").withType("DELETE"))
				.add(linkTo(methodOn(BookController.class).update(entity.getId(), BookMapper.convertEntityToDtoV2(entity))).withRel("update").withType("PUT"));
		
	}

	@Transactional
	public BookDTOv2 insert(BookDTOv2 dto) {

		dto.setStatus(BookStatus.AVAILABLE);
		
		Book entity = BookMapper.convertDtoToEntityV2(dto);

		entity.setAuthor(authorRepository.findById(dto.getAuthor().getId()).orElseThrow(
				() -> new ResourceNotFoundException("Resource AUTHOR not found. ID " + dto.getAuthor().getId())));

		entity.getCategories().clear();

		for (CategoryDTOv2 categoryDTO : dto.getCategories()) {

			Category category = categoryRepository.findById(categoryDTO.getId()).orElseThrow(
					() -> new ResourceNotFoundException("Resource CATEGORY not found. ID " + categoryDTO.getId()));

			entity.getCategories().add(category);

		}

		entity = bookRepository.save(entity);
		
		logger.info("Resource BOOK saved: " + entity.toString());

		return BookMapper.convertEntityToDtoV2(entity)
				.add(linkTo(methodOn(BookController.class).findById(entity.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(BookController.class).delete(entity.getId())).withRel("delete").withType("DELETE"))
				.add(linkTo(methodOn(BookController.class).update(entity.getId(), BookMapper.convertEntityToDtoV2(entity))).withRel("update").withType("PUT"));

	}

	@Transactional
	public BookDTOv2 update(Long id, BookDTOv2 dto) {

		Book entity = bookRepository.getReferenceById(id);
		
		dto.setStatus(entity.getStatus());
		
		dto.setId(id);

		entity = BookMapper.convertDtoToEntityV2(dto);

		entity = bookRepository.save(entity);
		
		logger.info("Resource BOOK updated: " + entity.toString());

		return BookMapper.convertEntityToDtoV2(entity)
				.add(linkTo(methodOn(BookController.class).findById(entity.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(BookController.class).delete(entity.getId())).withRel("delete").withType("DELETE"))
				.add(linkTo(methodOn(BookController.class).update(entity.getId(), BookMapper.convertEntityToDtoV2(entity))).withRel("update").withType("PUT"));

	}

	public void delete(Long id) {

		if (bookRepository.existsById(id)) {
			bookRepository.deleteById(id);
		} else {
			throw new ResourceNotFoundException("Resource BOOK not found. ID " + id);
		}

		logger.info("Resource BOOK deleted: ID " + id);

	}

}
