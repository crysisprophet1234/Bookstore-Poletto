package com.poletto.bookstore.controllers.v2;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poletto.bookstore.dto.v2.CategoryDTOv2;
import com.poletto.bookstore.services.v2.CategoryService;

@RestController("CategoryControllerV2")
@RequestMapping (value = "/categories/v2")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public ResponseEntity<Set<CategoryDTOv2>> findAll() {
		Set<CategoryDTOv2> categories = categoryService.findAll();
		return ResponseEntity.ok().body(categories);
	}
	
	@GetMapping(value = "/paged")
	public ResponseEntity<Page<CategoryDTOv2>> findAllPaged(Pageable pageable) {
		Page<CategoryDTOv2> list = categoryService.findAllPaged(pageable);		
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryDTOv2> findById(@PathVariable Long id) {
		CategoryDTOv2 dto = categoryService.findById(id);
		return ResponseEntity.ok().body(dto);
	}

}
