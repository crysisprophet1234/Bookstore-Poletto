package com.poletto.bookstore.exceptions.handler;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.poletto.bookstore.exceptions.DatabaseException;
import com.poletto.bookstore.exceptions.InvalidStatusException;
import com.poletto.bookstore.exceptions.ObjectNotValidException;
import com.poletto.bookstore.exceptions.ResourceNotFoundException;
import com.poletto.bookstore.exceptions.UnauthorizedException;
import com.poletto.bookstore.exceptions.exceptionresponse.ExceptionResponse;
import com.poletto.bookstore.util.StackTraceFormatter;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	//TODO refactor this...

	private static final Logger logger = LoggerFactory.getLogger(CustomizedResponseEntityExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {

		ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(),
				ex.getLocalizedMessage(), request.getDescription(false));

		logger.warn(exceptionResponse.toString() + clientInfo(request));
		logger.error(StackTraceFormatter.stackTraceFormatter(ex));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(DatabaseException.class)
	public final ResponseEntity<ExceptionResponse> handleDatabaseException(Exception ex, WebRequest request) {

		ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Somente livros nunca reservados podem ser deletados",
				ex.getLocalizedMessage(), request.getDescription(false));

		logger.warn(exceptionResponse.toString() + clientInfo(request));
		logger.error(StackTraceFormatter.stackTraceFormatter(ex));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler(UnauthorizedException.class)
	public final ResponseEntity<ExceptionResponse> handleUnauthorizedException(Exception ex, WebRequest request) {

		ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(),
				ex.getLocalizedMessage(), request.getDescription(false));

		logger.warn(exceptionResponse.toString() + clientInfo(request));
		logger.error(StackTraceFormatter.stackTraceFormatter(ex));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);

	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleNotFoundException(Exception ex, WebRequest request) {

		ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.NOT_FOUND, ex.getMessage(),
				ex.getLocalizedMessage(), request.getDescription(false));

		logger.warn(exceptionResponse.toString() + clientInfo(request));
		logger.error(StackTraceFormatter.stackTraceFormatter(ex));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(ObjectNotValidException.class)
	public final ResponseEntity<ExceptionResponse> handleObjectNotValidException(Exception ex, WebRequest request) {

		ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(),
				ex.getLocalizedMessage(), request.getDescription(false));

		logger.warn(exceptionResponse.toString() + clientInfo(request));
		logger.error(StackTraceFormatter.stackTraceFormatter(ex));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.UNPROCESSABLE_ENTITY);

	}

	@ExceptionHandler(EntityNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleEntityNotFoundException(Exception ex, WebRequest request) {

		ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.NOT_FOUND, ex.getMessage(),
				ex.getLocalizedMessage(), request.getDescription(false));

		logger.warn(exceptionResponse.toString() + clientInfo(request));
		logger.error(StackTraceFormatter.stackTraceFormatter(ex));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(InvalidStatusException.class)
	public final ResponseEntity<ExceptionResponse> handleInvalidStatusException(Exception ex, WebRequest request) {

		ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST, ex.getMessage(),
				ex.getLocalizedMessage(), request.getDescription(false));

		logger.warn(exceptionResponse.toString() + clientInfo(request));
		logger.error(StackTraceFormatter.stackTraceFormatter(ex));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(ConstraintViolationException.class)
	public final ResponseEntity<ExceptionResponse> handleConstraintViolation(Exception ex, WebRequest request) {

		ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST, ex.getMessage(),
				ex.getLocalizedMessage(), request.getDescription(false));

		logger.warn(exceptionResponse.toString() + clientInfo(request));
		logger.error(StackTraceFormatter.stackTraceFormatter(ex));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);

	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		StringBuilder sb = new StringBuilder();

		BindingResult results = ex.getBindingResult();

		for (FieldError e : results.getFieldErrors()) {

			sb.append(e.getObjectName()).append(": campo [").append(e.getField()).append("] ")
					.append(e.getDefaultMessage()).append(". Valor passado: ");

			if (e.getRejectedValue() != null) {
				sb.append(e.getRejectedValue().equals("") ? "(empty)" : ("'" + e.getRejectedValue()) + "'");
			} else {
				sb.append("(null)");
			}

		}

		ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNPROCESSABLE_ENTITY, sb.toString(),
				ex.getLocalizedMessage(), request.getDescription(false));

		logger.warn(exceptionResponse.toString() + clientInfo(request));
		logger.error(StackTraceFormatter.stackTraceFormatter(ex));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.UNPROCESSABLE_ENTITY);

	}

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
		sb.append("Request Parameters: ").append(requestParams.toString()).append("]");

		return sb.toString();
	}

	private List<String> getUserRoles(WebRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof UserDetails) {
				return ((UserDetails) principal).getAuthorities().stream().map(GrantedAuthority::getAuthority)
						.collect(Collectors.toList());
			}
		}
		return Collections.emptyList();
	}

}
