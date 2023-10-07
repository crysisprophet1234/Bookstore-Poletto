package com.poletto.bookstore.services.v3.mocks;

import java.time.LocalDate;

import com.poletto.bookstore.dto.v2.BookDTOv2;
import com.poletto.bookstore.dto.v2.CategoryDTOv2;
import com.poletto.bookstore.entities.Author;
import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.enums.BookStatus;

public class BookMocks {

	public static BookDTOv2 bookMockDto(Long id) {
		
		return new BookDTOv2(
				id,
				"book" + id,
				LocalDate.of(2023, 1, 1),
				"http://imageurl" + id,
				BookStatus.AVAILABLE,
				new Author(1L, "Author Author", "Earth")
		);
		
	}

	public static BookDTOv2 bookMockDto() {
		
		return new BookDTOv2(
				1L,
				"book",
				LocalDate.of(2023, 1, 1),
				"http://imageurl",
				BookStatus.AVAILABLE,
				new Author(1L, "Author Author", "Earth")
		);
		
	}
	
	public static Book bookMockEntity() {
		
		return new Book(
				1L,
				"book",
				BookStatus.AVAILABLE,
				new Author(1L, "Author Author", "Earth"),
				LocalDate.of(2023,  1, 1),
				"http://imageurl"
		);
		
	}
	
	public static Book bookMockEntity(Long id) {
		
		return new Book(
				id,
				"book" + id,
				BookStatus.AVAILABLE,
				new Author(1L, "Author Author", "Earth"),
				LocalDate.of(2023,  1, 1),
				"http://imageurl" + id
		);
		
	}
	
	
	public static BookDTOv2 insertBookMockDto() {
		BookDTOv2 bookDTOv2 = new BookDTOv2();
		bookDTOv2.setName("Book name");
		bookDTOv2.setReleaseDate(LocalDate.of(2023, 1, 1));
		bookDTOv2.setImgUrl("http://imageurl");
		bookDTOv2.setAuthor(new Author(1L, "Author Author", "Earth"));
		bookDTOv2.getCategories().add(new CategoryDTOv2(1L, "Category1"));
		bookDTOv2.getCategories().add(new CategoryDTOv2(2L, "Category2"));
		bookDTOv2.getCategories().add(new CategoryDTOv2(3L, "Category3"));
		return bookDTOv2;	
	}
	
	
}
