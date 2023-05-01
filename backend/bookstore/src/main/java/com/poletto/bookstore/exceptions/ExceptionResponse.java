package com.poletto.bookstore.exceptions;

import java.io.Serializable;
import java.util.Date;

public class ExceptionResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date timestamp;
	private String title;
	private String details;
	private String path;

	public ExceptionResponse(Date timestamp, String title, String details, String path) {
		this.timestamp = timestamp;
		this.title = title;
		this.details = details;
		this.path = path;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "ExceptionResponse [timestamp=" + timestamp + ", title=" + title + ", details="
				+ details + ", path=" + path + "]";
	}
	
}