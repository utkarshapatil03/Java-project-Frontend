package com.ticketbooking.book.dto;

public class BookTrainRequestDto {

	private int scheduleTrainId;

	private int userId;

	private int trainSeatId;

	private int totalSeat;

	public int getScheduleTrainId() {
		return scheduleTrainId;
	}

	public void setScheduleTrainId(int scheduleTrainId) {
		this.scheduleTrainId = scheduleTrainId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTotalSeat() {
		return totalSeat;
	}

	public void setTotalSeat(int totalSeat) {
		this.totalSeat = totalSeat;
	}

	public int getTrainSeatId() {
		return trainSeatId;
	}

	public void setTrainSeatId(int trainSeatId) {
		this.trainSeatId = trainSeatId;
	}

}
