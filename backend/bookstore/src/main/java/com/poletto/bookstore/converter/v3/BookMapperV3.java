package com.poletto.bookstore.converter.v3;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import com.poletto.bookstore.dto.v3.BookDto;
import com.poletto.bookstore.dto.v3.BookUpdateDto;
import com.poletto.bookstore.entities.Book;

@Component
@Mapper(componentModel = "spring")
public interface BookMapperV3 {
	
	BookMapperV3 INSTANCE = Mappers.getMapper(BookMapperV3.class);

	@Mapping(target = "id", source = "id")
	BookDto bookToBookDto(Book book);
	
	@Mapping(target = "id", source = "id")
	Book bookDtoToBook(BookDto bookDto);
	
	@Mapping(target = "id", source = "book.id")
	@Mapping(target = "status", source = "book.status")
	@Mapping(target = "title", source = "bookUpdateDto.title")
    @Mapping(target = "description", source = "bookUpdateDto.description")
    @Mapping(target = "language", source = "bookUpdateDto.language")
    @Mapping(target = "numberOfPages", source = "bookUpdateDto.numberOfPages")
    @Mapping(target = "publicationDate", source = "bookUpdateDto.publicationDate")
    @Mapping(target = "imgUrl", source = "bookUpdateDto.imgUrl")
    @Mapping(target = "author", source = "bookUpdateDto.author")
	@Mapping(target = "categories", source = "bookUpdateDto.categories")
	Book updateBookWithBookUpdateDto(Book book, BookUpdateDto bookUpdateDto);
	
}
