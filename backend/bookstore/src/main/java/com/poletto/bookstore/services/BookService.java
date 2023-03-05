package com.poletto.bookstore.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.converter.ModelMapperConverter;
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

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Transactional(readOnly = true)
	public List<BookDTO> findAll(Integer booked) {
		
		List<Book> list;

		switch (booked) {

		case 0:
			list = bookRepository.findByStatus(BookStatus.AVAILABLE.toString());
			break;

		case 1:
			list = bookRepository.findByStatus(BookStatus.BOOKED.toString());
			break;

		default:
			list = bookRepository.findAll();
			break;

		}

		return list.stream().map(x -> ModelMapperConverter.parseObject(x, BookDTO.class)).toList();
	}
	
	@Transactional(readOnly = true)
	public Page<BookDTO> findAllPaged(Pageable pageable, Long categoryId, String name) {
		
		List<Category> categories = (categoryId == 0) ? null : Arrays.asList(categoryRepository.getReferenceById(categoryId));
		
		var bookPage = bookRepository.findPaged(categories, name, pageable);
		
		bookRepository.findProductsWithCategories(bookPage.getContent());

		var bookDtoPage = bookPage.map(p -> ModelMapperConverter.parseObject(p, BookDTO.class));
		
		
		
		return bookDtoPage;
		
	}

	@Transactional(readOnly = true)
	public BookDTO findById(Long id) {
		Optional<Book> obj = bookRepository.findById(id);
		Book book = obj.orElseThrow(() -> new ResourceNotFoundException(id));
		return new BookDTO(book);
	}

	@Transactional
	public BookDTO insert(BookDTO dto) {

		Book entity = new Book();
		
		dto.setStatus("AVAILABLE");

		copyDtoToEntity(dto, entity);

		entity = bookRepository.save(entity);

		return new BookDTO(entity);

	}

	@Transactional
	public BookDTO update(Long id, BookDTO dto) {

		try {

			Book entity = bookRepository.getReferenceById(id);

			copyDtoToEntity(dto, entity);

			entity = bookRepository.save(entity);

			return new BookDTO(entity);

		} catch (EntityNotFoundException e) {

			throw new ResourceNotFoundException(id);

		}

	}

	public void delete(Long id) {

		try {

			bookRepository.deleteById(id);

		} catch (EmptyResultDataAccessException e) {

			throw new ResourceNotFoundException(id);

		} catch (DataIntegrityViolationException e) {

			throw new DatabaseException("Integrity violation");

		}

	}

	private void copyDtoToEntity(BookDTO dto, Book entity) {

		entity.setName(dto.getName());
		entity.setReleaseDate(dto.getReleaseDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setAuthor(authorRepository.findById(dto.getAuthor().getId())
				.orElseThrow(() -> new ResourceNotFoundException(dto.getAuthor().getId())));

		entity.setStatus(BookStatus.valueOf(dto.getStatus()));

		entity.getCategories().clear();

		for (CategoryDTO categoryDTO : dto.getCategories()) {
			Category category = categoryRepository.getReferenceById(categoryDTO.getId());
			entity.getCategories().add(category);
		}

	}

}
