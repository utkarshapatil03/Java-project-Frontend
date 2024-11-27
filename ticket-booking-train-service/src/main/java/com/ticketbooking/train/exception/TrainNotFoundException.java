package com.ticketbooking.train.exception;

public class TrainNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TrainNotFoundException() {
		super("Train not found in database");
	}

	public TrainNotFoundException(String message) {
		super(message);
	}

}
