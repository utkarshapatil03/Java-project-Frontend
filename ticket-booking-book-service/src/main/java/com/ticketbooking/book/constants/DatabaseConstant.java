package com.ticketbooking.book.constants;

public class DatabaseConstant {

	public static enum BookingStatus {
		AVAILABLE(0, "Available"), CONFIRMED(1, "Confirmed"), CANCELLED(2, "Cancelled"), WAITING(3, "Waiting");

		private int code;
		private String status;

		private BookingStatus(int code, String status) {
			this.status = status;
			this.code = code;
		}

		public int value() {
			return this.code;
		}

		public String status() {
			return this.status;
		}

		public static String getStatusString(int code) {
			for (BookingStatus bookingStatus : BookingStatus.values()) {
				if (bookingStatus.value() == code) {
					return bookingStatus.status();
				}
			}
			return null; // or throw an exception, or return a default value
		}

	}

}
