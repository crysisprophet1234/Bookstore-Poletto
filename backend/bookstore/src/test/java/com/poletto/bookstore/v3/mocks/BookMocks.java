package com.poletto.bookstore.v3.mocks;

import java.time.LocalDate;
import java.util.Set;

import com.poletto.bookstore.dto.v2.AuthorDTOv2;
import com.poletto.bookstore.dto.v2.BookDTOv2;
import com.poletto.bookstore.dto.v2.CategoryDTOv2;
import com.poletto.bookstore.dto.v3.AuthorDto;
import com.poletto.bookstore.dto.v3.BookDto;
import com.poletto.bookstore.dto.v3.CategoryDto;
import com.poletto.bookstore.entities.Author;
import com.poletto.bookstore.entities.Book;
import com.poletto.bookstore.entities.Category;
import com.poletto.bookstore.entities.enums.BookStatus;

public class BookMocks {

	public static BookDTOv2 bookMockDto(Long id) {
		
		return new BookDTOv2(
				id,
				"book" + id,
				LocalDate.of(2023, 1, 1),
				"http://imageurl" + id,
				BookStatus.ACTIVE,
				new AuthorDTOv2(1L, "Author Author")
		);
		
	}

	public static BookDTOv2 bookMockDto() {
		
		return new BookDTOv2(
				1L,
				"book",
				LocalDate.of(2023, 1, 1),
				"http://imageurl",
				BookStatus.ACTIVE,
				new AuthorDTOv2(1L, "Author Author")
		);
		
	}
	
	public static Book bookMockEntity() {
		
		Book book = new Book();
		book.setId(1L);
		book.setTitle("book");
		book.setDescription("book mock description");
		book.setLanguage("english");
		book.setNumberOfPages(111);
		book.setPublicationDate(LocalDate.of(2023,  1, 1));
		book.setImgUrl("http://imageurl");
		book.setStatus(BookStatus.ACTIVE);
		book.setAuthor(new Author(1L, "Author Author", "Earth"));
		
		return book;
		
	}
	
	public static Book bookMockEntity(Long id) {
		
		Book book = new Book();
		book.setId(id);
		book.setTitle("book " + id);
		book.setDescription("book mock description " + id);
		book.setLanguage("english");
		book.setNumberOfPages(111);
		book.setPublicationDate(LocalDate.of(2023,  1, 1));
		book.setImgUrl("http://imageurl");
		book.setStatus(BookStatus.ACTIVE);
		book.setAuthor(new Author(1L, "Author Author", "Earth"));
		
		return book;
		
	}
	
	
	public static BookDto insertBookMockDto() {
		
		BookDto bookDto = new BookDto();
		
		bookDto.setTitle("book mock");
		bookDto.setDescription("book mock description");
		bookDto.setLanguage("english");
		bookDto.setNumberOfPages(111);
		bookDto.setPublicationDate(LocalDate.of(2023,  1, 1));
		bookDto.setImgUrl("http://imageurl");
		bookDto.setStatus(BookStatus.ACTIVE);
		bookDto.setAuthor(authorMockDto());
		bookDto.getCategories().addAll(Set.of(categoryMockDto(1L), categoryMockDto(2L)));
		
		return bookDto;	
		
	}
	
	public static Author authorMockEntity() {
		
		Author author = new Author();
		author.setId(1L);
		author.setName("Mock Name");
		author.setNationality("Mockland");
		
		return author;
		
		
	}
	
	public static Author authorMockEntity(Long authorId) {
		
		Author author = new Author();
		author.setId(authorId);
		author.setName("Mock Name");
		author.setNationality("Mockland");
		
		return author;
			
	}
	
	public static AuthorDto authorMockDto() {
		
		AuthorDto authorDto = new AuthorDto();
		authorDto.setId(1L);
		authorDto.setName("Mock Name");
		authorDto.setNationality("Mockland");
		
		return authorDto;
		
	}
	
	public static AuthorDto authorMockDto(Long authorId) {
		
		AuthorDto authorDto = new AuthorDto();
		authorDto.setId(authorId);
		authorDto.setName("Mock Name");
		authorDto.setNationality("Mockland");
		
		return authorDto;
		
	}
	
	public static Category categoryMockEntity() {
		
	    Category category = new Category();
	    category.setId(1L);
	    category.setName("Mock Category");
	    
	    return category;
	}

	public static Category categoryMockEntity(Long categoryId) {
		
	    Category category = new Category();
	    category.setId(categoryId);
	    category.setName("Mock Category");
	    
	    return category;
	}

	public static CategoryDto categoryMockDto() {
		
	    CategoryDto categoryDto = new CategoryDto();
	    categoryDto.setId(1L);
	    categoryDto.setName("Mock Category");
	    
	    return categoryDto;
	}

	public static CategoryDto categoryMockDto(Long categoryId) {
		
	    CategoryDto categoryDto = new CategoryDto();
	    categoryDto.setId(categoryId);
	    categoryDto.setName("Mock Category");
	    
	    return categoryDto;
	}
	
}
