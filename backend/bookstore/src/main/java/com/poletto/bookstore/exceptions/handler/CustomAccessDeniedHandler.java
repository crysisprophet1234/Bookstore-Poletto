package com.poletto.bookstore.exceptions.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.poletto.bookstore.converter.CustomObjectMapper;
import com.poletto.bookstore.exceptions.exceptionresponse.ExceptionResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        ExceptionResponse errorResponse = new ExceptionResponse(HttpStatus.FORBIDDEN, e.getMessage(), e.getLocalizedMessage(), request.getRequestURI());

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getOutputStream().print(new CustomObjectMapper().writeValueAsString(errorResponse));
    }
}

