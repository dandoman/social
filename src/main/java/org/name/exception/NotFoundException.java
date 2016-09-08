package org.name.exception;

public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = -2643265770144267025L;
	
	public NotFoundException(String message){
		super(message);
	}
}
