package com.ticketbooking.train.exception;

public class TrainSeatNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TrainSeatNotFoundException() {
		super("Seat not found in database");
	}

	public TrainSeatNotFoundException(String message) {
		super(message);
	}

}
