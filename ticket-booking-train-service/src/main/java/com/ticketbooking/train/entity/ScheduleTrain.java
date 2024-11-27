package com.ticketbooking.train.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ScheduleTrain {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private int trainId;

	private String scheduleTrainId; // unique ref id

	private String scheduleTime; // epochTime

	private int status;

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
