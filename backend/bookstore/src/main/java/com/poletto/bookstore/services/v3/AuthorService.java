package com.poletto.bookstore.services.v3;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.controllers.v3.AuthorController;
import com.poletto.bookstore.controllers.v3.BookController;
import com.poletto.bookstore.converter.DozerMapperConverter;
import com.poletto.bookstore.dto.v2.AuthorDTOv2;
import com.poletto.bookstore.entities.Author;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.repositories.v2.AuthorRepository;

@Service("AuthorServiceV3")
public class AuthorService {
	
	//TODO impossível criar links de livros por autor, categoryId não recebe valor válido

	private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

	@Autowired
	private AuthorRepository authorRepository;

	@Cacheable(value = "authorsList")
	@Transactional(readOnly = true)
	public List<AuthorDTOv2> findAll() {

		List<Author> entitiesList = authorRepository.findAll();

		logger.info("Resoucer AUTHOR list found: " + entitiesList);

		List<AuthorDTOv2> dtosList = DozerMapperConverter.parseListObjects(entitiesList, AuthorDTOv2.class);

		dtosList.stream().forEach(dto -> dto
				.add(linkTo(methodOn(AuthorController.class).findById(dto.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(BookController.class).findAllPaged(0, 12, "asc", "name", 0L, dto.getName(), "all")).withRel("BOOKS BY AUTHOR").withType("GET")));

		return dtosList;

	}

	@Cacheable(value = "authorsPage")
	@Transactional(readOnly = true)
	public Page<AuthorDTOv2> findAllPaged(Pageable pageable) {

		Page<Author> entitiesPage = authorRepository.findAll(pageable);

		logger.info("Resource AUTHOR page found: " + "PAGE NUMBER [" + entitiesPage.getNumber() + "] - CONTENT: " + entitiesPage.getContent());

		Page<AuthorDTOv2> dtosPage = entitiesPage.map(x -> DozerMapperConverter.parseObject(x, AuthorDTOv2.class));

		dtosPage.stream().forEach(dto -> dto
				.add(linkTo(methodOn(AuthorController.class).findById(dto.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(BookController.class).findAllPaged(0, 12, "asc", "name", 0L, dto.getName(), "all")).withRel("BOOKS BY AUTHOR").withType("GET")));

		return dtosPage;

	}

	@Cacheable(value = "author", key = "#id")
	@Transactional(readOnly = true)
	public AuthorDTOv2 findById(Long id) {

		Author entity = authorRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Resource AUTHOR not found. ID " + id));

		logger.info("Resource AUTHOR found: " + entity.toString());

		return DozerMapperConverter.parseObject(entity, AuthorDTOv2.class)
				.add(linkTo(methodOn(AuthorController.class).findById(entity.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(BookController.class).findAllPaged(0, 12, "asc", "name", 0L, entity.getName(), "all")).withRel("BOOKS BY AUTHOR").withType("GET"));

	}

}
