package com.poletto.bookstore.exceptions.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;

import com.poletto.bookstore.converter.CustomObjectMapper;
import com.poletto.bookstore.exceptions.ExceptionResponse.ExceptionResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomJwtExceptionHandler extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public void handleAuthenticationException(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
		
		ExceptionResponse errorResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), ex.getLocalizedMessage(), request.getRequestURI());

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.getOutputStream().print(new CustomObjectMapper().writeValueAsString(errorResponse));
	}

}
