package com.poletto.bookstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poletto.bookstore.dto.v1.CategoryDTO;
import com.poletto.bookstore.services.CategoryService;

@Controller
@RequestMapping (value = "/categories/v1")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public ResponseEntity<Page<CategoryDTO>> findAll(Pageable pageable) {
		Page<CategoryDTO> list = categoryService.findAllPaged(pageable);		
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
		CategoryDTO dto = categoryService.findById(id);
		return ResponseEntity.ok().body(dto);
	}

}
