package com.poletto.bookstore.mocks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.poletto.bookstore.dto.v1.CategoryDTOv1;
import com.poletto.bookstore.dto.v1.BookDTOv1;
import com.poletto.bookstore.entities.Author;
import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.Category;
import com.poletto.bookstore.entities.enums.BookStatus;

public class MockBook {

	public Book mockEntity() {
		return mockEntity(0);
	}
	
	public BookDTOv1 mockDTO() {
		return mockDTO(0);
	}
	
	 public List<Book> mockEntityList() {
	        List<Book> books = new ArrayList<Book>();
	        for (int i = 0; i < 15; i++) {
	            books.add(mockEntity(i));
	        }
	        return books;
	 }
	 
	 public List<BookDTOv1> mockBookDTO() {
	        List<BookDTOv1> booksDto = new ArrayList<BookDTOv1>();
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
	
	public BookDTOv1 mockDTO(Integer number) {
		BookDTOv1 book = new BookDTOv1();
		book.setId(number.longValue());
		book.setName("Book Test " + number);
		book.setImgUrl("Img Url Test " + number);
		book.setReleaseDate(LocalDate.now().minusWeeks(number));
		book.setStatus((number & 1) == 0 ? BookStatus.BOOKED : BookStatus.AVAILABLE);
		book.setAuthor(new Author(number.longValue(), "Author Name Test " + number, (number & 1) == 0 ? "terrestre" : "terráqueo"));
		book.getCategories().add(new CategoryDTOv1(number.longValue(), "Category Test " + number));
		return book;
	}

}
