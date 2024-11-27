package com.ticketbooking.book.dto;

import java.util.List;

public class ScheduleTrainResponseDto extends CommonApiResponse {

	private List<TrainDetail> train;

	public List<TrainDetail> getTrain() {
		return train;
	}

	public void setTrain(List<TrainDetail> train) {
		this.train = train;
	}

}
