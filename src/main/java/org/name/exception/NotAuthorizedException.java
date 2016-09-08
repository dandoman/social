package org.name.exception;

public class NotAuthorizedException extends RuntimeException {
	private static final long serialVersionUID = -6329498993848224843L;
	public NotAuthorizedException(String message){
		super(message);
	}
}
