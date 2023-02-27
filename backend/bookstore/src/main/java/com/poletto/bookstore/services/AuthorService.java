package com.poletto.bookstore.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.dto.AuthorDTO;
import com.poletto.bookstore.entities.Author;
import com.poletto.bookstore.repositories.AuthorRepository;
import com.poletto.bookstore.services.exceptions.ResourceNotFoundException;

@Service
public class AuthorService {
	
	@Autowired
	private AuthorRepository authorRepository;
	
	@Transactional(readOnly = true)
	public List<AuthorDTO> findAll() {
		List<Author> list = authorRepository.findAll();
		return list.stream().map(x -> new AuthorDTO(x)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public Page<AuthorDTO> findAllPaged(Pageable pageable) {
		Page<Author> list = authorRepository.findAll(pageable);
		return list.map(x -> new AuthorDTO(x));
	}
	
	@Transactional(readOnly = true)
	public AuthorDTO findById(Long id) {
		Optional<Author> obj = authorRepository.findById(id);
		Author entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));
		return new AuthorDTO(entity);
	}

}
