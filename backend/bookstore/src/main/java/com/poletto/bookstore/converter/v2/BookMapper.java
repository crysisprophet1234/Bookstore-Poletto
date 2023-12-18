package com.poletto.bookstore.converter.v2;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import com.poletto.bookstore.dto.v2.BookDTOv2;
import com.poletto.bookstore.entities.Book;

@Component
@Mapper(componentModel = "spring")
public interface BookMapper {
	
	BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

	@Mapping(target = "id", source = "id")
	BookDTOv2 bookToBookDto(Book book);
	
	@Mapping(target = "id", source = "id")
	Book bookDtoToBook(BookDTOv2 bookDto);

}
