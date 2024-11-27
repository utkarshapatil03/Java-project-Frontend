package com.ticketbooking.user.exception;

public class UserNotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotFoundException() {
		super("User not found in database");
	}
	
	public UserNotFoundException(String message) {
		super(message);
	}

}
