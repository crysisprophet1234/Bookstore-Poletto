package com.poletto.bookstore.exceptions.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.poletto.bookstore.converter.CustomObjectMapper;
import com.poletto.bookstore.exceptions.exceptionresponse.ExceptionResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		
		ExceptionResponse errorResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", authException.getLocalizedMessage(), request.getRequestURI());
		
		final String authHeader = request.getHeader("Authorization");
		
		if (authHeader == null) {

			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getOutputStream().print(new CustomObjectMapper().writeValueAsString(errorResponse));
		
		}
		
	}
}
