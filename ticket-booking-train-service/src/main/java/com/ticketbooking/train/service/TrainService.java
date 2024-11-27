package com.ticketbooking.train.service;

import java.util.List;

import com.ticketbooking.train.entity.Train;

public interface TrainService {
	
	Train addTrain(Train train);
	Train getTrainById(int trainId);
	Train updateTrain(Train train);
	List<Train> getAllTrain();
	List<Train> getTrainByStatus(int trainStatus);
	List<Train> getTrainByFromAndToLocation(int fromLocationId, int toLocationId, int status);
	Train getTrainByNumber(String trainNumber);
	List<Train> getAllTrainsByNumber(String trainNumber, int status);
	

}
