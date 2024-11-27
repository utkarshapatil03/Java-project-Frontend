package com.ticketbooking.train.dto;

import java.util.List;

public class TrainDetailResponseDto extends CommonApiResponse {

	private List<TrainDetail> train;

	public List<TrainDetail> getTrain() {
		return train;
	}

	public void setTrain(List<TrainDetail> train) {
		this.train = train;
	}

}
