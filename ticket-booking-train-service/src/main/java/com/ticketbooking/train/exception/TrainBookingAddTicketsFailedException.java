package com.ticketbooking.train.exception;

public class TrainBookingAddTicketsFailedException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TrainBookingAddTicketsFailedException() {
		super("Failed to add Scheduled Train Tickets in Booking Table");
	}

	public TrainBookingAddTicketsFailedException(String message) {
		super(message);
	}

}
