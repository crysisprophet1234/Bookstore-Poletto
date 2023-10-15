package com.poletto.bookstore.services.v3;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.controllers.v2.BookController;
import com.poletto.bookstore.controllers.v2.CategoryController;
import com.poletto.bookstore.converter.DozerMapperConverter;
import com.poletto.bookstore.dto.v2.CategoryDTOv2;
import com.poletto.bookstore.entities.Category;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.repositories.v2.CategoryRepository;

@Service("CategoryServiceV3")
public class CategoryService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Cacheable(value = "categoriesList")
	@Transactional (readOnly = true)
	public Set<CategoryDTOv2> findAll() {
		
		List<Category> entitiesList = categoryRepository.findAll();
		
		logger.info("Resource CATEGORY list found: " + DozerMapperConverter.parseListObjects(entitiesList, CategoryDTOv2.class));
		
		Set<CategoryDTOv2> dtosList = DozerMapperConverter.parseListObjects(entitiesList, CategoryDTOv2.class).stream().collect(Collectors.toSet());
		
		dtosList.stream().forEach(dto -> dto
				.add(linkTo(methodOn(CategoryController.class).findById(dto.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(BookController.class).findAllPaged(0, 12, "asc", "name", dto.getId(), "", "all")).withRel("BOOKS WITH THIS CATEGORY").withType("GET")));
		
		return dtosList;
		
	}
	
	@Cacheable(value = "categoriesPage")
	@Transactional(readOnly = true)
	public Page<CategoryDTOv2> findAllPaged(Pageable pageable) {
		
		Page<Category> entitiesPage = categoryRepository.findAll(pageable);
		
		logger.info("Resource CATEGORY page found: " + "PAGE NUMBER [" + entitiesPage.getNumber() + "] - CONTENT: " + DozerMapperConverter.parseListObjects(entitiesPage.getContent(), CategoryDTOv2.class));
		
		Page<CategoryDTOv2> dtosPage = entitiesPage.map(x -> DozerMapperConverter.parseObject(x, CategoryDTOv2.class));
		
		dtosPage.stream().forEach(dto -> dto
				.add(linkTo(methodOn(CategoryController.class).findById(dto.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(BookController.class).findAllPaged(0, 12, "asc", "name", dto.getId(), "", "all")).withRel("BOOKS WITH THIS CATEGORY").withType("GET")));
		
		return dtosPage;
		
	}
	
	@Cacheable(value = "category", key = "#id")
	@Transactional(readOnly = true)
	public CategoryDTOv2 findById(Long id) {
		
		Category entity = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource CATEGORY not found. ID " + id));
		
		logger.info("Resource CATEGORY found: " + entity);
		
		return DozerMapperConverter.parseObject(entity, CategoryDTOv2.class)
				.add(linkTo(methodOn(BookController.class).findAllPaged(0, 12, "asc", "name", entity.getId(), "", "all")).withRel("BOOKS WITH THIS CATEGORY").withType("GET"));
		
	}
	
}
