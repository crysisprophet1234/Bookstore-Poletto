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
import com.poletto.bookstore.dto.v2.CategoryDTOv2;
import com.poletto.bookstore.dto.v3.CategoryDto;
import com.poletto.bookstore.entities.Category;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.repositories.v3.CategoryRepository;
import com.poletto.bookstore.services.v3.CategoryService;

@Service("CategoryServiceV3")
public class CategoryServiceImpl implements CategoryService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional (readOnly = true)
	public List<CategoryDto> findAll() {
		
		List<Category> entitiesList = categoryRepository.findAll();
		
		logger.info("Resource CATEGORY list found: " + DozerMapperConverter.parseListObjects(entitiesList, CategoryDTOv2.class));
		
		List<CategoryDto> dtosList = DozerMapperConverter.parseListObjects(entitiesList, CategoryDto.class);
		
		return dtosList;
		
	}
	
	@Transactional(readOnly = true)
	public Page<CategoryDto> findAllPaged(Pageable pageable) {
		
		Page<Category> entitiesPage = categoryRepository.findAll(pageable);
		
		logger.info("Resource CATEGORY page found: " + "PAGE NUMBER [" + entitiesPage.getNumber() + "] - CONTENT: " + DozerMapperConverter.parseListObjects(entitiesPage.getContent(), CategoryDTOv2.class));
		
		Page<CategoryDto> dtosPage = entitiesPage.map(x -> DozerMapperConverter.parseObject(x, CategoryDto.class));
		
		return dtosPage;
		
	}
	
	@Transactional(readOnly = true)
	public CategoryDto findById(Long id) {

		Category entity = categoryRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Resource CATEGORY not found. ID " + id));
		
		logger.info("Resource CATEGORY found: " + entity);
		
		return DozerMapperConverter.parseObject(entity, CategoryDto.class);
		
	}

	@Override
	public CategoryDto insert(CategoryDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CategoryDto update(Long id, CategoryDto dto) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
