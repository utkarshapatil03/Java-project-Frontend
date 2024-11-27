package com.ticketbooking.book.exception;

public class BookingNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BookingNotFoundException() {
		super("Booking not found in database");
	}

	public BookingNotFoundException(String message) {
		super(message);
	}

}
