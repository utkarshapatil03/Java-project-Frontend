package com.ticketbooking.book.dto;

import java.util.List;

public class BookingResponseDto extends CommonApiResponse {

	private List<BookingResponse> bookings;

	public List<BookingResponse> getBookings() {
		return bookings;
	}

	public void setBookings(List<BookingResponse> bookings) {
		this.bookings = bookings;
	}

}
