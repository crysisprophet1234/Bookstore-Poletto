package com.poletto.bookstore.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.converter.DozerMapperConverter;
import com.poletto.bookstore.dto.v1.CategoryDTO;
import com.poletto.bookstore.entities.Category;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional (readOnly = true)
	public Set<CategoryDTO> findAll() {
		
		List<Category> list = categoryRepository.findAll();
		
		logger.info("Resource CATEGORY list found: " + DozerMapperConverter.parseListObjects(list, CategoryDTO.class));
		
		return list.stream().map(x -> DozerMapperConverter.parseObject(x, CategoryDTO.class)).collect(Collectors.toSet());
		
	}
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(Pageable pageable) {
		
		Page<Category> categoryPage = categoryRepository.findAll(pageable);
		
		logger.info("Resource CATEGORY page found: " + "PAGE NUMBER [" + categoryPage.getNumber() + "] - CONTENT: " + DozerMapperConverter.parseListObjects(categoryPage.getContent(), CategoryDTO.class));
		
		return categoryPage.map(x -> DozerMapperConverter.parseObject(x, CategoryDTO.class));
		
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		
		Optional<Category> obj = categoryRepository.findById(id);
		
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Resource CATEGORY not found. ID " + id));
		
		CategoryDTO dto = DozerMapperConverter.parseObject(entity, CategoryDTO.class);
		
		logger.info("Resource CATEGORY found: " + dto);
		
		return dto;
		
	}
	
}
