package com.ticketbooking.book.dto;

public class BookingResponse {

	// train detail
	private int trainId;
	private String trainName;
	private String trainNumber;
	private double seatPrice;
	private String fromLocation;
	private String toLocation;

	// train seat detail
	private int trainSeatId;
	private String trainSeat;

	// booking detail
	private int scheduleTrainId;
	private String scheduleTrainTime;
	private String bookingId; // unique generated booking id
	private int bookId; // primary key of Train Booking
	private int userId;
	private String status;
	private String bookingTime;

	// user detail
	private String username;
	private String mobile;

	public int getTrainId() {
		return trainId;
	}

	public void setTrainId(int trainId) {
		this.trainId = trainId;
	}

	public String getTrainName() {
		return trainName;
	}

	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}

	public String getTrainNumber() {
		return trainNumber;
	}

	public void setTrainNumber(String trainNumber) {
		this.trainNumber = trainNumber;
	}

	public double getSeatPrice() {
		return seatPrice;
	}

	public void setSeatPrice(double seatPrice) {
		this.seatPrice = seatPrice;
	}

	public String getFromLocation() {
		return fromLocation;
	}

	public void setFromLocation(String fromLocation) {
		this.fromLocation = fromLocation;
	}

	public String getToLocation() {
		return toLocation;
	}

	public void setToLocation(String toLocation) {
		this.toLocation = toLocation;
	}

	public int getTrainSeatId() {
		return trainSeatId;
	}

	public void setTrainSeatId(int trainSeatId) {
		this.trainSeatId = trainSeatId;
	}

	public String getTrainSeat() {
		return trainSeat;
	}

	public void setTrainSeat(String trainSeat) {
		this.trainSeat = trainSeat;
	}

	public int getScheduleTrainId() {
		return scheduleTrainId;
	}

	public void setScheduleTrainId(int scheduleTrainId) {
		this.scheduleTrainId = scheduleTrainId;
	}

	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(String bookingTime) {
		this.bookingTime = bookingTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getScheduleTrainTime() {
		return scheduleTrainTime;
	}

    public void setScheduleTrainTime(String scheduleTrainTime) {
		this.scheduleTrainTime = scheduleTrainTime;
	}
	
}
