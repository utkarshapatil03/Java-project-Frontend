package com.ticketbooking.book.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;

@Entity
@Builder
public class TrainBooking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private int trainId;

	private int scheduleTrainId;

	private String bookingId;

	private int trainSeatId;

	private String trainSeat;

	private String bookingTime;

	private int userId;

	private int status;

	public TrainBooking(int id, int trainId, int scheduleTrainId, String bookingId, int trainSeatId, String trainSeat,
			String bookingTime, int userId, int status) {
		super();
		this.id = id;
		this.trainId = trainId;
		this.scheduleTrainId = scheduleTrainId;
		this.bookingId = bookingId;
		this.trainSeatId = trainSeatId;
		this.trainSeat = trainSeat;
		this.bookingTime = bookingTime;
		this.userId = userId;
		this.status = status;
	}

	public TrainBooking() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTrainId() {
		return trainId;
	}

	public void setTrainId(int trainId) {
		this.trainId = trainId;
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

	public int getTrainSeatId() {
		return trainSeatId;
	}

	public void setTrainSeatId(int trainSeatId) {
		this.trainSeatId = trainSeatId;
	}

	public String getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(String bookingTime) {
		this.bookingTime = bookingTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTrainSeat() {
		return trainSeat;
	}

	public void setTrainSeat(String trainSeat) {
		this.trainSeat = trainSeat;
	}

}
