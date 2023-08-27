package com.poletto.bookstore.services.v2;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

import com.poletto.bookstore.controllers.v2.BookController;
import com.poletto.bookstore.controllers.v2.CategoryController;
import com.poletto.bookstore.converter.DozerMapperConverter;
import com.poletto.bookstore.dto.v2.CategoryDTOv2;
import com.poletto.bookstore.entities.Category;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.repositories.CategoryRepository;

@Service("CategoryServiceV2")
public class CategoryService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional (readOnly = true)
	public Set<CategoryDTOv2> findAll() {
		
		List<Category> list = categoryRepository.findAll();
		
		logger.info("Resource CATEGORY list found: " + DozerMapperConverter.parseListObjects(list, CategoryDTOv2.class));
		
		Set<CategoryDTOv2> dtos = DozerMapperConverter.parseListObjects(list, CategoryDTOv2.class).stream().collect(Collectors.toSet());
		
		dtos.forEach(x -> x.add(linkTo(methodOn(CategoryController.class).findById(x.getId())).withSelfRel().withType("GET")));
		dtos.forEach(x -> x.add(linkTo(methodOn(BookController.class).findAllPaged(0, 12, "asc", "name", x.getId(), "", "all")).withRel("BOOKS WITH THIS CATEGORY").withType("GET")));
		
		return dtos;
		
	}
	
	@Transactional(readOnly = true)
	public Page<CategoryDTOv2> findAllPaged(Pageable pageable) {
		
		Page<Category> categoryPage = categoryRepository.findAll(pageable);
		
		logger.info("Resource CATEGORY page found: " + "PAGE NUMBER [" + categoryPage.getNumber() + "] - CONTENT: " + DozerMapperConverter.parseListObjects(categoryPage.getContent(), CategoryDTOv2.class));
		
		Page<CategoryDTOv2> dtos = categoryPage.map(x -> DozerMapperConverter.parseObject(x, CategoryDTOv2.class));
		
		dtos.forEach(x -> x.add(linkTo(methodOn(CategoryController.class).findById(x.getId())).withSelfRel().withType("GET")));
		dtos.forEach(x -> x.add(linkTo(methodOn(BookController.class).findAllPaged(0, 12, "asc", "name", x.getId(), "", "all")).withRel("BOOKS WITH THIS CATEGORY").withType("GET")));
		
		return dtos;
		
	}
	
	@Transactional(readOnly = true)
	public CategoryDTOv2 findById(Long id) {
		
		Optional<Category> obj = categoryRepository.findById(id);
		
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Resource CATEGORY not found. ID " + id));
		
		CategoryDTOv2 dto = DozerMapperConverter.parseObject(entity, CategoryDTOv2.class);
		
		dto.add(linkTo(methodOn(BookController.class).findAllPaged(0, 12, "asc", "name", dto.getId(), "", "all")).withRel("BOOKS WITH THIS CATEGORY").withType("GET"));
		
		logger.info("Resource CATEGORY found: " + dto);
		
		return dto;
		
	}
	
}
