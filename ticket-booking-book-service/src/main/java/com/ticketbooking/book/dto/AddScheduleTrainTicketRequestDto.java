package com.ticketbooking.book.dto;

import java.util.List;

public class AddScheduleTrainTicketRequestDto {
	
	private int scheduleTrainId;
	
	private List<TrainSeat> trainSeats;

	public int getScheduleTrainId() {
		return scheduleTrainId;
	}

	public void setScheduleTrainId(int scheduleTrainId) {
		this.scheduleTrainId = scheduleTrainId;
	}

	public List<TrainSeat> getTrainSeats() {
		return trainSeats;
	}

	public void setTrainSeats(List<TrainSeat> trainSeats) {
		this.trainSeats = trainSeats;
	}
	
	

}
