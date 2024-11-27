package com.ticketbooking.train.dto;

import java.util.List;

import com.ticketbooking.train.entity.TrainSeat;

public class TrainDetail {

	private int id;
	private String name;
	private String number;
	private int totalCoach;
	private int totalSeatInEachCoach;
	private double seatPrice;
	private String fromLocation;
	private String toLocation;
	private List<TrainSeat> trainSeats;

	// for scheduled train
	private int scheduleId;  // primary key for schedule train
	private String scheduleTrainId; // unique ref id
	private String scheduleTime; // epochTime

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getTotalCoach() {
		return totalCoach;
	}

	public void setTotalCoach(int totalCoach) {
		this.totalCoach = totalCoach;
	}

	public int getTotalSeatInEachCoach() {
		return totalSeatInEachCoach;
	}

	public void setTotalSeatInEachCoach(int totalSeatInEachCoach) {
		this.totalSeatInEachCoach = totalSeatInEachCoach;
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

	public String getScheduleTrainId() {
		return scheduleTrainId;
	}

	public void setScheduleTrainId(String scheduleTrainId) {
		this.scheduleTrainId = scheduleTrainId;
	}

	public String getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public List<TrainSeat> getTrainSeats() {
		return trainSeats;
	}

	public void setTrainSeats(List<TrainSeat> trainSeats) {
		this.trainSeats = trainSeats;
	}

	public int getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}
	
	

}
