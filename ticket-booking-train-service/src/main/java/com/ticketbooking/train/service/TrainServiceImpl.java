package com.ticketbooking.train.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketbooking.train.dao.TrainDao;
import com.ticketbooking.train.entity.Train;

@Service
public class TrainServiceImpl implements TrainService {

	@Autowired
	private TrainDao trainDao;

	@Override
	public Train addTrain(Train train) {
		return trainDao.save(train);
	}

	@Override
	public Train getTrainById(int trainId) {
		return trainDao.findById(trainId).get();
	}

	@Override
	public Train updateTrain(Train train) {
		return trainDao.save(train);
	}

	@Override
	public List<Train> getAllTrain() {
		return trainDao.findAll();
	}

	@Override
	public List<Train> getTrainByStatus(int trainStatus) {
		return trainDao.findByStatus(trainStatus);
	}

	@Override
	public List<Train> getTrainByFromAndToLocation(int fromLocationId, int toLocationId, int status) {
		return trainDao.findByFromLocationIdAndToLocationIdAndStatus(fromLocationId, toLocationId, status);
	}

	@Override
	public Train getTrainByNumber(String trainNumber) {
		return trainDao.findByNumber(trainNumber);
	}

	@Override
	public List<Train> getAllTrainsByNumber(String trainNumber, int status) {
		return trainDao.findByNumberContainingIgnoreCaseAndStatus(trainNumber, status);
	}

}
