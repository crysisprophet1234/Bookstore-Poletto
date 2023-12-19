package com.poletto.bookstore.services.v3;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.dto.v3.AuthorDto;

@Service("AuthorServiceV3")
public interface AuthorService {
	
	@Transactional(readOnly = true)
	List<AuthorDto> findAll();
	
	@Transactional(readOnly = true)
	Page<AuthorDto> findAllPaged(Pageable pageable);
	
	@Transactional(readOnly = true)
	AuthorDto findById(Long id);
	
	@Transactional
	AuthorDto insert(AuthorDto dto);
	
	@Transactional
	AuthorDto update(Long id, AuthorDto dto); 

}
