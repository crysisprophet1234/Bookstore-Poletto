package com.poletto.bookstore.converter.v3;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import com.poletto.bookstore.dto.v3.AddressDto;
import com.poletto.bookstore.dto.v3.AuthorDto;
import com.poletto.bookstore.entities.Address;
import com.poletto.bookstore.entities.Author;

@Component
@Mapper(componentModel = "spring")
public interface AuthorMapperV3 {
	
	AuthorMapperV3 INSTANCE = Mappers.getMapper(AuthorMapperV3.class);

	AuthorDto authorToAuthorDto(Author author);

	@Mapping(target = "books", ignore = true)
	Author authorDtoToAuthor(AuthorDto authorDto);

}
