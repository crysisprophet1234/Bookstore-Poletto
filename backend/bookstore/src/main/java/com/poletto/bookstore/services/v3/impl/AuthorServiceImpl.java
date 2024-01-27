package com.poletto.bookstore.services.v3.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.converter.DozerMapperConverter;
import com.poletto.bookstore.dto.v3.AuthorDto;
import com.poletto.bookstore.entities.Author;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.repositories.v3.AuthorRepository;
import com.poletto.bookstore.services.v3.AuthorService;

@Service("AuthorServiceV3")
public class AuthorServiceImpl implements AuthorService {
	
	//TODO #28

	private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);

	@Autowired
	private AuthorRepository authorRepository;

	@Transactional(readOnly = true)
	public List<AuthorDto> findAll() {

		List<Author> entitiesList = authorRepository.findAll();

		logger.info("Resoucer AUTHOR list found: " + entitiesList);

		List<AuthorDto> dtosList = DozerMapperConverter.parseListObjects(entitiesList, AuthorDto.class);

		return dtosList;

	}

	@Transactional(readOnly = true)
	public Page<AuthorDto> findAllPaged(Pageable pageable) {

		Page<Author> entitiesPage = authorRepository.findAll(pageable);

		logger.info("Resource AUTHOR page found: " + "PAGE NUMBER [" + entitiesPage.getNumber() + "] - CONTENT: " + entitiesPage.getContent());

		Page<AuthorDto> dtosPage = entitiesPage.map(x -> DozerMapperConverter.parseObject(x, AuthorDto.class));

		return dtosPage;

	}

	@Transactional(readOnly = true)
	public AuthorDto findById(Long id) {

		Author entity = authorRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Resource AUTHOR not found. ID " + id));

		logger.info("Resource AUTHOR found: " + entity.toString());

		return DozerMapperConverter.parseObject(entity, AuthorDto.class);

	}

	@Override
	public AuthorDto insert(AuthorDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthorDto update(Long id, AuthorDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
