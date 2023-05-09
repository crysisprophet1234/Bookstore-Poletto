package com.poletto.bookstore.converter.custom;

import org.springframework.stereotype.Service;

import com.poletto.bookstore.converter.DozerMapperConverter;
import com.poletto.bookstore.dto.v1.CategoryDTO;
import com.poletto.bookstore.dto.v1.BookDTO;
import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.Category;
import com.poletto.bookstore.entities.enums.BookStatus;

@Service
public class BookMapper {

	public static BookDTO convertEntityToDto(Book book) {
		
		BookDTO bookDto = new BookDTO();
		bookDto.setId(book.getId());
		bookDto.setName(book.getName());
		bookDto.setImgUrl(book.getImgUrl());
		bookDto.setReleaseDate(book.getReleaseDate());
		bookDto.setStatus((book.getStatus()));
		bookDto.setAuthor(book.getAuthor());
		
		bookDto.getCategories().clear();
		
		for (Category category : book.getCategories()) {
			bookDto.getCategories().add(DozerMapperConverter.parseObject(category, CategoryDTO.class));
		}
		

		return bookDto;

	}

	public static Book convertDtoToEntity(BookDTO dto) {

		Book book = new Book();
		book.setId(dto.getId());
		book.setName(dto.getName());
		book.setImgUrl(dto.getImgUrl());
		book.setReleaseDate(dto.getReleaseDate());
		book.setStatus(BookStatus.valueOf(dto.getStatus()));
		book.setAuthor(dto.getAuthor());
		
		book.getCategories().clear();
		
		for (CategoryDTO categoryDTO : dto.getCategories()) {
			book.getCategories().add(DozerMapperConverter.parseObject(categoryDTO, Category.class));
		}

		return book;

	}
	
	public static com.poletto.bookstore.dto.v2.BookDTO convertEntityToDtoV2(Book book) {
		
		com.poletto.bookstore.dto.v2.BookDTO bookDto = new com.poletto.bookstore.dto.v2.BookDTO();
		bookDto.setId(book.getId());
		bookDto.setName(book.getName());
		bookDto.setImgUrl(book.getImgUrl());
		bookDto.setReleaseDate(book.getReleaseDate());
		bookDto.setStatus((book.getStatus()));
		bookDto.setAuthor(book.getAuthor());
		
		bookDto.getCategories().clear();
		
		for (Category category : book.getCategories()) {
			bookDto.getCategories().add(DozerMapperConverter.parseObject(category, CategoryDTO.class));
		}
		

		return bookDto;

	}

}
