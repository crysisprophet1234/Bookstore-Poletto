package com.poletto.bookstore.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.converter.DozerMapperConverter;
import com.poletto.bookstore.dto.AuthorDTO;
import com.poletto.bookstore.entities.Author;
import com.poletto.bookstore.repositories.AuthorRepository;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;

@Service
public class AuthorService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);
	
	@Autowired
	private AuthorRepository authorRepository;
	
	@Transactional(readOnly = true)
	public List<AuthorDTO> findAll() {
		
		List<Author> list = authorRepository.findAll();
		
		logger.info("Resoucer AUTHOR list found: " + list);
		
		return list.stream().map(x -> DozerMapperConverter.parseObject(x, AuthorDTO.class)).collect(Collectors.toList());
		
	}
	
	@Transactional(readOnly = true)
	public Page<AuthorDTO> findAllPaged(Pageable pageable) {
		
		Page<Author> authorPage = authorRepository.findAll(pageable);
		
		logger.info("Resource AUTHOR page found: " + "PAGE NUMBER [" + authorPage.getNumber() + "] - CONTENT: " + authorPage.getContent());
		
		return authorPage.map(x -> DozerMapperConverter.parseObject(x, AuthorDTO.class));
		
	}
	
	@Transactional(readOnly = true)
	public AuthorDTO findById(Long id) {
		
		Optional<Author> obj = authorRepository.findById(id);
		
		Author entity = obj.orElseThrow(() -> new ResourceNotFoundException("Resource AUTHOR not found. ID " + id));
		
		logger.info("Resource AUTHOR found: " + entity.toString());
		
		return DozerMapperConverter.parseObject(entity, AuthorDTO.class);
		
	}

}
