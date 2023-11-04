package com.poletto.bookstore.util;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class StackTraceFormatter {

	public static String stackTraceFormatter(Exception ex) {

		StringBuilder sb = new StringBuilder();

		sb.append(ExceptionUtils.getMessage(ex));

		for (String error : ExceptionUtils.getStackFrames(ex)) {
			if (error.contains("Caused") || error.contains("com.poletto")) {
				sb.append("\n" + error);
			}
		}

		return sb.toString();

	}

	public static String stackTraceFormatter(Exception ex, String... exclusion) {
		
	    StringBuilder sb = new StringBuilder();
	    sb.append(ExceptionUtils.getMessage(ex));

	    for (String error : ExceptionUtils.getStackFrames(ex)) {
	    	
	        if (error.contains("Caused") || error.contains("com.poletto")) {
	            
	            for (String exclusionItem : exclusion) {
	            	
	                if (!exclusionItem.contains(error)) {
	                	sb.append("\n").append(error);
	                    break;
	                }
	                
	            }
	            
	        }
	        
	    }

	    return sb.toString();
	}

}
