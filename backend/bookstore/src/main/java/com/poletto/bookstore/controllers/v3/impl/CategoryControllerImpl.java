package com.poletto.bookstore.controllers.v3.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poletto.bookstore.controllers.v3.CategoryController;
import com.poletto.bookstore.dto.v3.CategoryDto;
import com.poletto.bookstore.services.v3.CategoryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController("CategoryControllerV3")
@RequestMapping (value = "/categories/v3")
@Tag(name = "Category Controller V3", description = "Endpoints related to category resources management")
public class CategoryControllerImpl implements CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public ResponseEntity<List<CategoryDto>> findAll() {
		
		List<CategoryDto> categories = categoryService.findAll();
		
		return ResponseEntity.ok().body(categories);
		
	}
	
	@GetMapping(value = "/paged")
	public ResponseEntity<Page<CategoryDto>> findAllPaged(Pageable pageable) {
		
		Page<CategoryDto> list = categoryService.findAllPaged(pageable);	
		
		return ResponseEntity.ok().body(list);
		
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryDto> findById(@PathVariable Long id) {
		
		CategoryDto dto = categoryService.findById(id);
		
		return ResponseEntity.ok().body(dto);
		
	}

	@Override
	public ResponseEntity<CategoryDto> insert(@RequestBody CategoryDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<CategoryDto> update(@Min(1) Long id, @Valid CategoryDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

}