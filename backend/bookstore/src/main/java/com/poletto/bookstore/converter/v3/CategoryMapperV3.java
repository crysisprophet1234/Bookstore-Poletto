package com.poletto.bookstore.converter.v3;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import com.poletto.bookstore.dto.v3.CategoryDto;
import com.poletto.bookstore.entities.Category;

@Component
@Mapper(componentModel = "spring")
public interface CategoryMapperV3 {
	
	CategoryMapperV3 INSTANCE = Mappers.getMapper(CategoryMapperV3.class);

	CategoryDto categoryToCategoryDto(Category category);

	@Mapping(target = "books", ignore = true)
	Category categoryDtoToCategory(CategoryDto categoryDto);
	
}
