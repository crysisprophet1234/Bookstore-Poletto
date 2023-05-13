package com.poletto.bookstore.controllers.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poletto.bookstore.dto.v1.CategoryDTOv1;
import com.poletto.bookstore.services.v1.CategoryServiceV1;

@Controller
@RequestMapping (value = "/categories/v2")
public class CategoryControllerV2 {
	
	@Autowired
	private CategoryServiceV1 categoryService;
	
	@GetMapping
	public ResponseEntity<Page<CategoryDTOv1>> findAll(Pageable pageable) {
		Page<CategoryDTOv1> list = categoryService.findAllPaged(pageable);		
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryDTOv1> findById(@PathVariable Long id) {
		CategoryDTOv1 dto = categoryService.findById(id);
		return ResponseEntity.ok().body(dto);
	}

}
