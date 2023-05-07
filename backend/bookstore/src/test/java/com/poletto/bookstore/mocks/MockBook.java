package com.poletto.bookstore.mocks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.poletto.bookstore.dto.v1.CategoryDTO;
import com.poletto.bookstore.dto.v1.BookDTO;
import com.poletto.bookstore.entities.Author;
import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.Category;
import com.poletto.bookstore.entities.enums.BookStatus;

public class MockBook {

	public Book mockEntity() {
		return mockEntity(0);
	}
	
	public BookDTO mockDTO() {
		return mockDTO(0);
	}
	
	 public List<Book> mockEntityList() {
	        List<Book> books = new ArrayList<Book>();
	        for (int i = 0; i < 15; i++) {
	            books.add(mockEntity(i));
	        }
	        return books;
	 }
	 
	 public List<BookDTO> mockBookDTO() {
	        List<BookDTO> booksDto = new ArrayList<BookDTO>();
	        for (int i = 0; i < 15; i++) {
	        	booksDto.add(mockDTO(i));
	        }
	        return booksDto;
	 }
	
	public Book mockEntity(Integer number) {
		Book book = new Book();
		book.setId(number.longValue());
		book.setName("Book Test " + number);
		book.setImgUrl("Img Url Test " + number);
		book.setReleaseDate(LocalDate.now().minusWeeks(number));
		book.setStatus((number & 1) == 0 ? BookStatus.BOOKED : BookStatus.AVAILABLE);
		book.setAuthor(new Author(number.longValue(), "Author Name Test " + number, (number & 1) == 0 ? "terrestre" : "terráqueo"));
		book.getCategories().add(new Category(number.longValue(), "Category Test " + number));
		return book;
	}
	
	public BookDTO mockDTO(Integer number) {
		BookDTO book = new BookDTO();
		book.setId(number.longValue());
		book.setName("Book Test " + number);
		book.setImgUrl("Img Url Test " + number);
		book.setReleaseDate(LocalDate.now().minusWeeks(number));
		book.setStatus((number & 1) == 0 ? BookStatus.BOOKED : BookStatus.AVAILABLE);
		book.setAuthor(new Author(number.longValue(), "Author Name Test " + number, (number & 1) == 0 ? "terrestre" : "terráqueo"));
		book.getCategories().add(new CategoryDTO(number.longValue(), "Category Test " + number));
		return book;
	}

}
