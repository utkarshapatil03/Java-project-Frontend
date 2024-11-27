package com.ticketbooking.location.exception;

public class LocationNotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LocationNotFoundException() {
		super("Location not found in database");
	}
	
	public LocationNotFoundException(String message) {
		super(message);
	}

}
