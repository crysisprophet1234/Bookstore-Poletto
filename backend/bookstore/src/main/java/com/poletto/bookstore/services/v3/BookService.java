package com.poletto.bookstore.services.v3;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.dto.v3.BookDto;

@Service("BookServiceV3")
public interface BookService {
	
	@Transactional(readOnly = true)
	Page<BookDto> findAll(Pageable pageable, Long categoryId, String name, String status);
	
	@Transactional(readOnly = true)
	BookDto findById(Long id);
	
	@Transactional
	BookDto insert(BookDto dto);
	
	@Transactional
	BookDto update(Long id, BookDto dto);
	
	@Transactional
	void delete(Long id);

}
