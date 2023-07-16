package com.poletto.bookstore.exceptions.ExceptionResponse;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ExceptionResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private String path;

	public ExceptionResponse(HttpStatus status, String message, String debugMessage, String path) {
		timestamp = LocalDateTime.now();
		this.status = status;
		this.message = message;
		this.debugMessage = debugMessage;
		this.path = path;
	}

	public HttpStatus getStatus() {
		return status;
	}



	public void setStatus(HttpStatus status) {
		this.status = status;
	}



	public LocalDateTime getTimestamp() {
		return timestamp;
	}



	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}



	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	public String getDebugMessage() {
		return debugMessage;
	}



	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}



	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "ExceptionResponse [status=" + status + ", timestamp=" + timestamp + ", message=" + message
				+ ", path=" + path + "]";
	}
	
}