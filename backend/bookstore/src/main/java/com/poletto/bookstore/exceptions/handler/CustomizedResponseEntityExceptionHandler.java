package com.poletto.bookstore.exceptions.handler;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.poletto.bookstore.exceptions.DatabaseException;
import com.poletto.bookstore.exceptions.ExceptionResponse;
import com.poletto.bookstore.exceptions.InvalidStatusException;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.exceptions.UnauthorizedException;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(ResponseEntityExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {

		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),  ex.getMessage(), request.getDescription(false));

		logger.warn(exceptionResponse.toString() + clientInfo(request));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(DatabaseException.class)
	public final ResponseEntity<ExceptionResponse> handleDatabaseException(Exception ex, WebRequest request) {
		
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),  ex.getMessage(), request.getDescription(false));

		logger.warn(exceptionResponse.toString() + clientInfo(request));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public final ResponseEntity<ExceptionResponse> handleUnauthorizedException(Exception ex, WebRequest request) {
		
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), HttpStatus.UNAUTHORIZED.toString(), ex.getMessage(), request.getDescription(false));

		logger.warn(exceptionResponse.toString() + clientInfo(request));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleNotFoundException(Exception ex, WebRequest request) {
		
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), HttpStatus.NOT_FOUND.toString(), ex.getMessage(), request.getDescription(false));

		logger.warn(exceptionResponse.toString() + clientInfo(request));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InvalidStatusException.class)
	public final ResponseEntity<ExceptionResponse> handleInvalidStatusException(Exception ex, WebRequest request) {
		
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), HttpStatus.BAD_REQUEST.toString(),  ex.getMessage(), request.getDescription(false));

		logger.warn(exceptionResponse.toString() + clientInfo(request));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	/*
	@ExceptionHandler(RequiredObjecIsNullException.class)
	public final ResponseEntity<ExceptionResponse> handleBadRequestException(Exception ex, WebRequest request) {

		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
				request.getDescription(false));

		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	*/	
	
	private String clientInfo(WebRequest request) {
	    StringBuilder sb = new StringBuilder();

	    String userAgent = request.getHeader("User-Agent");
	    sb.append(" [User-Agent: ").append(userAgent).append(", ");

	    try {
	    	
			String user = request.getUserPrincipal().getName();
			sb.append("User logged: ").append(user).append(", ");
			
			List<String> roles = getUserRoles(request);
		    sb.append("User roles: " + roles).append(", ");
		    
		    String sessionId = request.getSessionId();
		    sb.append("Session ID: ").append(sessionId).append(", ");
		    
		} catch (NullPointerException e) {
			sb.append("User logged: -/-, ");
		}
	    
	    Map<String, String[]> requestParams = request.getParameterMap();
	    sb.append("Request Parameters: ").append(requestParams).append("]");
	    
	    return sb.toString();
	}
	
	private List<String> getUserRoles(WebRequest request) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null) {
	        Object principal = authentication.getPrincipal();
	        if (principal instanceof UserDetails) {
	            return ((UserDetails) principal).getAuthorities().stream()
	                    .map(GrantedAuthority::getAuthority)
	                    .collect(Collectors.toList());
	        }
	    }
	    return Collections.emptyList();
	}

}

