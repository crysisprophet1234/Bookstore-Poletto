package com.poletto.bookstore.dto.v3;

import java.io.Serializable;

import com.poletto.bookstore.entities.enums.BookStatus;

import jakarta.validation.constraints.NotNull;

public class BookStatusUpdateDto extends BookUpdateDto implements Serializable {
	

	private static final long serialVersionUID = 3L;
	
	@NotNull
	BookStatus bookStatus;
	
	public BookStatusUpdateDto (){
		
	}

	public BookStatusUpdateDto(BookStatus bookStatus) {
		this.bookStatus = bookStatus;
	}

	public BookStatus getBookStatus() {
		return bookStatus;
	}

	public void setBookStatus(BookStatus bookStatus) {
		this.bookStatus = bookStatus;
	}

	@Override
	public String toString() {
		return "BookStatusUpdateDto [bookStatus=" + bookStatus + "]";
	}

}