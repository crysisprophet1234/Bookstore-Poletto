package com.poletto.bookstore.services.v3;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poletto.bookstore.controllers.v3.BookController;
import com.poletto.bookstore.controllers.v3.CategoryController;
import com.poletto.bookstore.converter.DozerMapperConverter;
import com.poletto.bookstore.dto.v2.CategoryDTOv2;
import com.poletto.bookstore.entities.Category;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.repositories.v3.CategoryRepository;


@Service("CategoryServiceV3")
public class CategoryService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional (readOnly = true)
	public List<CategoryDTOv2> findAll() {
		
		List<Category> entitiesList = categoryRepository.findAll();
		
		logger.info("Resource CATEGORY list found: " + DozerMapperConverter.parseListObjects(entitiesList, CategoryDTOv2.class));
		
		List<CategoryDTOv2> dtosList = DozerMapperConverter.parseListObjects(entitiesList, CategoryDTOv2.class);
		
		dtosList.stream().forEach(dto -> dto
				.add(linkTo(methodOn(CategoryController.class).findById(dto.getId())).withSelfRel().withType("GET"))
				.add(linkTo(methodOn(BookController.class).findAllPaged(0, 12, "asc", "name", dto.getId(), "", "all")).withRel("BOOKS WITH THIS CATEGORY").withType("GET")));
		
		return dtosList;
		
	}
	
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
	
	@Transactional(readOnly = true)
	public CategoryDTOv2 findById(Long id) {

		Category entity = categoryRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Resource CATEGORY not found. ID " + id));
		
		logger.info("Resource CATEGORY found: " + entity);
		
		return DozerMapperConverter.parseObject(entity, CategoryDTOv2.class)
				.add(linkTo(methodOn(BookController.class).findAllPaged(0, 12, "asc", "name", entity.getId(), "", "all")).withRel("BOOKS WITH THIS CATEGORY").withType("GET"));
		
	}
	
}
