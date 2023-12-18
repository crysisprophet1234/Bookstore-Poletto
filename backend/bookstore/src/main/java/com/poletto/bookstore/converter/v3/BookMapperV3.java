package com.poletto.bookstore.converter.v3;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import com.poletto.bookstore.dto.v3.BookDto;
import com.poletto.bookstore.entities.Book;

@Component
@Mapper(componentModel = "spring")
public interface BookMapperV3 {
	
	BookMapperV3 INSTANCE = Mappers.getMapper(BookMapperV3.class);

	@Mapping(target = "id", source = "id")
	BookDto bookToBookDto(Book book);
	
	@Mapping(target = "id", source = "id")
	Book bookDtoToBook(BookDto bookDto);
	
}
