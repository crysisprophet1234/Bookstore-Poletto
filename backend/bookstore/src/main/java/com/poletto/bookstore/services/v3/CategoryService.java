package com.poletto.bookstore.services.v3;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.dto.v3.CategoryDto;

@Service("CategoryServiceV3")
public interface CategoryService {
	
	@Transactional (readOnly = true)
	public List<CategoryDto> findAll();
	
	@Transactional(readOnly = true)
	public Page<CategoryDto> findAllPaged(Pageable pageable);
	
	@Transactional(readOnly = true)
	public CategoryDto findById(Long id);	
	
	@Transactional
	public CategoryDto insert(CategoryDto dto);
	
	@Transactional
	public CategoryDto update(Long id, CategoryDto dto);
	
}
