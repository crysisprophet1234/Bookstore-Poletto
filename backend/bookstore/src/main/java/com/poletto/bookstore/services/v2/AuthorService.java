package com.poletto.bookstore.services.v2;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.controllers.v2.AuthorController;
import com.poletto.bookstore.controllers.v2.BookController;
import com.poletto.bookstore.converter.DozerMapperConverter;
import com.poletto.bookstore.dto.v2.AuthorDTOv2;
import com.poletto.bookstore.entities.Author;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.repositories.v2.AuthorRepository;

@Service("AuthorServiceV2")
public class AuthorService {

	private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

	@Autowired
	private AuthorRepository authorRepository;

	@Transactional(readOnly = true)
	public List<AuthorDTOv2> findAll() {

		List<Author> list = authorRepository.findAll();

		logger.info("Resoucer AUTHOR list found: " + list);

		List<AuthorDTOv2> dtos = DozerMapperConverter.parseListObjects(list, AuthorDTOv2.class);

		dtos.stream().forEach(x -> x
				.add(linkTo(methodOn(AuthorController.class).findById(x.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(BookController.class).findAllPaged(0, 12, "asc", "name", 0L, x.getName(), "all")).withRel("BOOKS BY AUTHOR").withType("GET")));

		return dtos;

	}

	@Transactional(readOnly = true)
	public Page<AuthorDTOv2> findAllPaged(Pageable pageable) {

		Page<Author> authorPage = authorRepository.findAll(pageable);

		logger.info("Resource AUTHOR page found: " + "PAGE NUMBER [" + authorPage.getNumber() + "] - CONTENT: "
				+ authorPage.getContent());

		Page<AuthorDTOv2> dtos = authorPage.map(x -> DozerMapperConverter.parseObject(x, AuthorDTOv2.class));

		dtos.stream().forEach(x -> x
				.add(linkTo(methodOn(AuthorController.class).findById(x.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(BookController.class).findAllPaged(0, 12, "asc", "name", 0L, x.getName(), "all")).withRel("BOOKS BY AUTHOR").withType("GET")));

		return dtos;

	}

	@Transactional(readOnly = true)
	public AuthorDTOv2 findById(Long id) {

		Optional<Author> obj = authorRepository.findById(id);

		Author entity = obj.orElseThrow(() -> new ResourceNotFoundException("Resource AUTHOR not found. ID " + id));

		logger.info("Resource AUTHOR found: " + entity.toString());

		return DozerMapperConverter.parseObject(entity, AuthorDTOv2.class)
				.add(linkTo(methodOn(BookController.class).findAllPaged(0, 12, "asc", "name", 0L, entity.getName(), "all")).withRel("BOOKS BY AUTHOR").withType("GET"));

	}

}
