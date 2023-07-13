package com.poletto.bookstore.converter.custom;

import org.springframework.stereotype.Service;

import com.poletto.bookstore.converter.DozerMapperConverter;
import com.poletto.bookstore.dto.v1.BookDTOv1;
import com.poletto.bookstore.dto.v1.CategoryDTOv1;
import com.poletto.bookstore.dto.v2.BookDTOv2;
import com.poletto.bookstore.dto.v2.CategoryDTOv2;
import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.Category;
import com.poletto.bookstore.entities.enums.BookStatus;

@Service
public class BookMapper {

	public static BookDTOv1 convertEntityToDto(Book book) {
		
		BookDTOv1 bookDto = new BookDTOv1();
		bookDto.setId(book.getId());
		bookDto.setName(book.getName());
		bookDto.setImgUrl(book.getImgUrl());
		bookDto.setReleaseDate(book.getReleaseDate());
		bookDto.setStatus((book.getStatus()));
		bookDto.setAuthor(book.getAuthor());
		
		bookDto.getCategories().clear();
		
		for (Category category : book.getCategories()) {
			bookDto.getCategories().add(DozerMapperConverter.parseObject(category, CategoryDTOv1.class));
		}
		

		return bookDto;

	}

	public static Book convertDtoToEntity(BookDTOv1 dto) {

		Book book = new Book();
		book.setId(dto.getId());
		book.setName(dto.getName());
		book.setImgUrl(dto.getImgUrl());
		book.setReleaseDate(dto.getReleaseDate());
		book.setStatus(BookStatus.valueOf(dto.getStatus()));
		book.setAuthor(dto.getAuthor());
		
		book.getCategories().clear();
		
		for (CategoryDTOv1 categoryDTO : dto.getCategories()) {
			book.getCategories().add(DozerMapperConverter.parseObject(categoryDTO, Category.class));
		}

		return book;

	}
	
	public static BookDTOv2 convertEntityToDtoV2(Book book) {
		
		BookDTOv2 bookDto = new BookDTOv2();
		bookDto.setId(book.getId());
		bookDto.setName(book.getName());
		bookDto.setImgUrl(book.getImgUrl());
		bookDto.setReleaseDate(book.getReleaseDate());
		bookDto.setStatus((book.getStatus()));
		bookDto.setAuthor(book.getAuthor());
		
		bookDto.getCategories().clear();
		
		for (Category category : book.getCategories()) {
			bookDto.getCategories().add(DozerMapperConverter.parseObject(category, CategoryDTOv2.class));
		}
		

		return bookDto;

	}
	
	public static Book convertDtoToEntityV2(BookDTOv2 dto) {

		Book book = new Book();
		book.setId(dto.getId());
		book.setName(dto.getName());
		book.setImgUrl(dto.getImgUrl());
		book.setReleaseDate(dto.getReleaseDate());
		book.setStatus(BookStatus.valueOf(dto.getStatus()));
		book.setAuthor(dto.getAuthor());
		
		book.getCategories().clear();
		
		for (CategoryDTOv2 categoryDTO : dto.getCategories()) {
			book.getCategories().add(DozerMapperConverter.parseObject(categoryDTO, Category.class));
		}

		return book;

	}

}
