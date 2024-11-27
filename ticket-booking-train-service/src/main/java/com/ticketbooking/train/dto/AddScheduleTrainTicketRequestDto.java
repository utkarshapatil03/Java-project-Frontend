package com.ticketbooking.train.dto;

import java.util.List;

import com.ticketbooking.train.entity.TrainSeat;

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
