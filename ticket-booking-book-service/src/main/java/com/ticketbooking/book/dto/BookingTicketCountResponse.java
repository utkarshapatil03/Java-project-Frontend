package com.ticketbooking.book.dto;

public class BookingTicketCountResponse extends CommonApiResponse {

	private int availableTicketCount;

	private int waitingTicketCount;

	private int confirmedTicketCount;

	public int getAvailableTicketCount() {
		return availableTicketCount;
	}

	public void setAvailableTicketCount(int availableTicketCount) {
		this.availableTicketCount = availableTicketCount;
	}

	public int getWaitingTicketCount() {
		return waitingTicketCount;
	}

	public void setWaitingTicketCount(int waitingTicketCount) {
		this.waitingTicketCount = waitingTicketCount;
	}

	public int getConfirmedTicketCount() {
		return confirmedTicketCount;
	}

	public void setConfirmedTicketCount(int confirmedTicketCount) {
		this.confirmedTicketCount = confirmedTicketCount;
	}

}
